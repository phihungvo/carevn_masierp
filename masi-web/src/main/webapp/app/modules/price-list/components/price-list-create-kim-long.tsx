import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import PriceListFormKimLong from './price-list-form-kim-long';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import Flex from 'app/components/flex/flex';
import Button from 'app/components/button/button';
import { useNavigate } from 'react-router';
import { FORM } from 'app/shared/model/enumerations/form.model';
import PriceListCreateSuccessModals from '../modals/price-list-create-success-modals';
import Card from 'app/components/card/card';
import '../price-list.scss'

const { CREATE_QUOTATION } = MUTATION_KEY;

const PriceListCreateKimLong = () => {
  const navigate = useNavigate();

  const [isOpen, setIsOpen] = useState(false);

  const toggle = (): void => {
    setIsOpen(prev => !prev);
  };

  const toggleSuccess = (): void => {
    toggle();
    navigate(-1);
  };

  const isCreating = useIsMutating({
    mutationKey: [CREATE_QUOTATION],
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Chi tiết bảng báo giá</Typography>
      <Card className="card-template">
      <PriceListFormKimLong type="create" toggleSuccess={toggle} />
        <Flex gap={12} justify="end" className="btn-group-template mt">
          <Button outline className="btn-cancel-template" onClick={() => navigate(-1)}>
            Huỷ
          </Button>
          <Button color="primary" type="submit" form={FORM.PRICE_LIST} disabled={!!isCreating}>
            Tạo mới
          </Button>
        </Flex>
      </Card>
      <PriceListCreateSuccessModals isOpen={isOpen} toggle={toggle} toogleSuccess={toggleSuccess} />
    </div>
  );
};

export default PriceListCreateKimLong;
