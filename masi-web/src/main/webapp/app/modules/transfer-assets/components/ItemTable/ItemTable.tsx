import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import Flex from 'app/components/flex/flex';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import { useState } from 'react';
import './ItemTable.scss';
import ItemModel from '../ItemModel';
import { useFormContext } from 'react-hook-form';
import useEmployee from 'app/hooks/use-employee';
import FormSelect from 'app/components/form/form-select';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import { ITransferAssetTransferDetailsDto } from 'app/shared/model/transfer-assets.model';



const ItemTable = (props: any) => {

  const { filter, type, employeeOfFromDepartment, employeeOfToDepartment } = props;
  const methods = useFormContext();
  const { watch, setValue, control, setError, clearErrors } = methods;

  const fromDepartmentId = watch('fromDepartmentId')
  const watchListItem = watch('assetTransferDetailsDTOS');
  const [openModal, setOpenModal] = useState<boolean>(false);
  const toggleModal = () => setOpenModal(prev => !prev);
  const { useGetEmployeeProfilesQuery } = useEmployee;

  const removeItem = (id: any) => {
    const updatedListItems = (watchListItem ?? []).filter(item => item.inventoriesStorageId !== id);

    setValue('assetTransferDetailsDTOS', [...updatedListItems]);
  }

  const addItems = (items: any[]) => {
    const groupList = {}
    watchListItem?.forEach(item => {
      groupList[item.inventoriesStorageId] = item
    })
    const itemsAdd = items.map(item => {
      if (item.id in groupList) {
        return {...groupList[item.id]}
      } else {
        return {
          employeeToId: null,
          employeeFromId: item.itemInfo?.userId,
          attribute: {},
          inventoriesStorageId: item.id,
          inventoriesStorage: item
        } as ITransferAssetTransferDetailsDto
      }
    });

    setValue('assetTransferDetailsDTOS', [...itemsAdd]);
    if(itemsAdd.length > 0) {
      clearErrors('assetTransferDetailsDTOS');
    }
  }

  const columns: TableColumns<any> = [
    {
      header: { render: 'Tài sản/CC' },
      body: {
        render: ({ data }) => data?.inventoriesStorage?.code,
      },
    },
    {
      header: { render: 'NCC' },
      body: {
        render: ({ data }) => data?.inventoriesStorage?.item?.supplier?.name ?? '',
      },
    },
    {
      header: { render: 'ĐVT' },
      body: {
        render: ({ data }) => data?.inventoriesStorage?.item?.uom?.name ?? '',
      },
    },
    {
      header: { render: 'SL' },
      body: {
        render: ({ data }) => data?.inventoriesStorage?.quantity ?? 1,
      },
    },
    {
      header: { render: 'Từ NSD' },
      body: {
        render: ({ data, index }) => {
          const employee = employeeOfFromDepartment?.data?.find(item => item.id === data?.employeeFromId)
          return employee ? `${employee.employeeCode} - ${employee?.fullName || ''}` : '';
        },
      },
    },
    {
      header: { render: 'Đến NSD' },
      body: {
        render: ({ data, index }) => {
          if (type === 'detail') {
            const employee = employeeOfToDepartment?.data?.find(item => item.id === data.employeeToId)
            return employee ? `${employee.employeeCode} - ${employee?.fullName || ''}` : '';
          } else {
            return (<FormSelect
              control={control}
              key={data.inventoriesStorageId}
              id={`assetTransferDetailsDTOS.${index}.employeeToId`}
              name={`assetTransferDetailsDTOS.${index}.employeeToId`}
              options={employeeOfToDepartment?.data?.map(s => ({
                value: s?.id,
                label: `${s.employeeCode} - ${s?.fullName || ''}`,
              }))}
              placeholder="Vui lòng chọn"
            />)
          }
        },
      },
    },
    {
      header: { render: 'Ghi chú' },
      body: {
        render: ({ data, index }) => {
          if (type === "detail") {
            return data?.attribute?.description
          } else {
            return (<FormInputV2
              control={control}
              key={data.inventoriesStorageId}
              id={`assetTransferDetailsDTOS.${index}.attribute.description`}
              name={`assetTransferDetailsDTOS.${index}.attribute.description`}
            />)
          }
        }
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ data }) => (
          <Flex align="center">
            {type === 'detail' ? (<></>) :
              (<ButtonDelete
                onClick={() => { removeItem(data?.inventoriesStorageId); }}
              />)}
          </Flex>
        ),
      },
    },
  ];
  return (
    <Flex direction="column" gap={8}>
      <div className="item_table_header">
        <div className="item_table_header_left">
          <span>Tài sản</span>
          {type === 'detail' ? <></> : <ButtonAdd
            text="Chọn thêm tài sản"
            style={{
              marginLeft: "1rem",
            }}
            disabled={!fromDepartmentId}
            onClick={toggleModal}
          />}
        </div>
      </div>

      <TableV2<any>
        table_id="item_table"
        columns={columns}
        data={[...(watchListItem ?? [])]}
        className={{ table: 'at__table' }}
      />
      <ItemModel isOpen={openModal} toggle={toggleModal} handleOnOK={addItems} departmentId={fromDepartmentId}
        selectedIds={[...(watchListItem ?? [])].map(item => item.inventoriesStorageId)} />
    </Flex>
  );
};

export default ItemTable;
