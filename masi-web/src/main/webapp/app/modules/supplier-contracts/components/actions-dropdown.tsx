import ButtonDropDown from 'app/components/ButtonV2/ButtonDropdown';
import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Tooltip from 'app/components/tooltip/tooltip';
import { useAppSelector } from 'app/config/store';
import {
  ISupplierContract,
  SUPPLIER_CONTRACT_STATUS,
} from 'app/shared/model/supplier-contract.model';
import { useContext, useState } from 'react';
import { SupplierContractsContext } from '../supplier-contracts-storage-provider';
import { SupplierContractsStatusOptions } from '../supplier-contracts-mapping';
import { isHasPermission } from 'app/constants/common';

interface IActionsDropdownProps {
  data: ISupplierContract;
}

const icon_path = 'content/images/vuesax/linear/';

const ActionsDropdown = ({ data }: IActionsDropdownProps) => {
  const account = useAppSelector(state => state.authentication.account);

  const [closeDropdown, setCloseDropdown] = useState<boolean>(false);

  const {
    toggleChangeStatus,
    setSelectedRecord,
  } = useContext(SupplierContractsContext);

  const handleChangeStatus = (status) => {
    toggleChangeStatus(status);
    setSelectedRecord(data?.id);
  };

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const isDisabledDelete = () => {
    if (data?.createdBy === account.id) {
      if (data?.status === SUPPLIER_CONTRACT_STATUS.NEW) return false;
      return true;
    }
    return true;
  };

  return (
    <ButtonDropDown
      isClose={closeDropdown}
      items={[
        {
          children: (
            <Tooltip label={'In phiếu'} target={`btn-print`}>
              <ButtonV2
                id="btn-print"
                variant="text"
                left_section={
                  <img
                    src="content/images/vuesax/linear/printer.svg"
                    alt="print"
                  />
                }
              >
                In phiếu
              </ButtonV2>
            </Tooltip>
          ),
          onClick: () => {
            setCloseDropdown(true);
            setTimeout(() => window.print(), 100);
          },
          hidden: !isHasPermission(authorities, 'LOGISTICS_FACTORIES.EXPORT'),
        },
        ...SupplierContractsStatusOptions.filter(item=>item.value !== data.status).map(item=>{
            return {
                children: (
                  <Tooltip label={item.label} target={item.value}>
                    <ButtonV2
                      id={item.value}
                      variant="text"
                      left_section={
                        <img
                          src={item.image_icon}
                          alt="cancel"
                        />
                      }
                    >
                      { item.label }
                    </ButtonV2>
                  </Tooltip>
                ),
                onClick: () => {
                  setSelectedRecord(data?.id);
                  handleChangeStatus(item.value);
                },
                hidden: !isHasPermission(authorities, 'LOGISTICS_SUPPLIER_CONTRACTS.EDIT'),
            }
          })
      ]}
    >
      <img src={icon_path + 'more-v2.svg'} alt="more" />
    </ButtonDropDown>
  );
};

export default ActionsDropdown;
