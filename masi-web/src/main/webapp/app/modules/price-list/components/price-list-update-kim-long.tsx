import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import PriceListUpdateSuccessModals from '../modals/price-list-update-success-modals';
import { useNavigate } from 'react-router';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import { Button } from 'reactstrap';
import Flex from 'app/components/flex/flex';
import { FORM } from 'app/shared/model/enumerations/form.model';
import PriceListFormKimLong from './price-list-form-kim-long';
import Card from 'app/components/card/card';

const { UPDATE_QUOTATION } = MUTATION_KEY;

const PriceListUpdateKimLong = () => {
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
    mutationKey: [UPDATE_QUOTATION],
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Bảng báo giá</Typography>
      <Card className="card-template">
        <Flex direction="column">
          <Typography level={5}>Cập nhật bảng báo giá Kim Long</Typography>

          <PriceListFormKimLong type="update" toggleSuccess={toggle} />
        </Flex>

        <Flex gap={12} justify="end" className="btn-group-template">
          <Button outline className="btn-cancel-template" onClick={() => navigate(-1)}>
            Huỷ
          </Button>
          <Button color="primary" type="submit" form={FORM.PRICE_LIST} disabled={!!isCreating}>
            Cập nhật
          </Button>
        </Flex>
      </Card>

      <PriceListUpdateSuccessModals isOpen={isOpen} toggle={toggleSuccess} />
    </div>
  );
};

export default PriceListUpdateKimLong;
