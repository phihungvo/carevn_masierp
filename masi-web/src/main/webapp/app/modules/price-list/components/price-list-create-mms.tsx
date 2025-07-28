import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import PriceListCreateSuccessModals from '../modals/price-list-create-success-modals';
import PriceListMMSForm from './price-list-form-mms';
import Flex from 'app/components/flex/flex';
import Button from 'app/components/button/button';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { useNavigate } from 'react-router';
import Card from 'app/components/card/card';

const { CREATE_QUOTATION } = MUTATION_KEY;

const PriceListCreateMMS = () => {
  const navigate = useNavigate();

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  const toggleSuccess = () => {
    toggle();
    navigate(-1);
  };

  const isCreating = useIsMutating({
    mutationKey: [CREATE_QUOTATION],
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Bảng báo giá</Typography>

      <Card className="card-template">
        <Flex direction="column">
          <Typography level={5}>Tạo bảng báo giá MMS</Typography>

          <PriceListMMSForm type="create" toggleSuccess={toggle} />
        </Flex>

        <Flex gap={12} justify="end" className="btn-group-template">
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

export default PriceListCreateMMS;
