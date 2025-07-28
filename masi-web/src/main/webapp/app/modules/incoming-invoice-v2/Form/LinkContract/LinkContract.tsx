import FormError from 'app/components/form/form-error';
import { Typography } from 'app/components/typography/typography';
import { useState } from 'react';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import { IncomingInvoiceV2SchemaType } from '../../validation/incoming.validate';
import ContractItem from './ContractItem';
import ContractModal from './ContractModal';
import InventoryModal from './InventoryModal';

type Props = {};

const LinkContract = (props: Props) => {
  const [openInventoryModal, setOpenInventoryModal] = useState(false);
  const [openContractModal, setOpenContractModal] = useState(false);

  const [checkedObj, setCheckedObj] = useState<Record<string, boolean>>({})

  const toggleInventory = () => setOpenInventoryModal(!openInventoryModal);
  const toggleContracts = () => setOpenContractModal(!openContractModal);

  const { watch, formState: { errors }, setValue } = useFormContext<IncomingInvoiceV2SchemaType>()
  const inventoryCodes = watch('inventoryCodes')
  const supplierContractCode = watch('supplierContractCode')

  return (
    <section>
      <Row>
        <Typography level={6}>Liên kết</Typography>
      </Row>
      <Row>
        <Col md={4}>
          <ContractItem
            onAdd={toggleContracts}
            label="Hợp đồng"
            code={supplierContractCode}
            onRemove={() => setValue('supplierContractCode', '')}
          />
        </Col>
        <Col md={4}>
          <ContractItem
            onRemove={() => {
              setValue('inventoryIds', [])
              setValue('inventoryCodes', [])
              setCheckedObj({})
            }}
            onAdd={toggleInventory}
            label="Phiếu nhập kho"
            code={inventoryCodes?.join(', ')}
          />
        </Col>
      </Row>
      <Row>
        <Col md={4}>
          {errors?.supplierContractId && (
            <FormError message={errors?.supplierContractId?.message} />
          )}
        </Col>
        <Col md={4}>
          {errors?.inventoryIds && (
            <FormError message={errors?.inventoryIds?.message} />
          )}
        </Col>
      </Row>
      <InventoryModal
        openModal={openInventoryModal}
        setOpenModal={setOpenInventoryModal}
        toggle={toggleInventory}
        checkedObj={checkedObj}
        setCheckedObj={setCheckedObj}
      />
      <ContractModal
        openModal={openContractModal}
        setOpenModal={setOpenContractModal}
        toggle={toggleContracts}
      />
    </section>
  );
};

export default LinkContract;
