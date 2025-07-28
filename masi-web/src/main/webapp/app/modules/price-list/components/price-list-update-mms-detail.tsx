import { Typography } from 'app/components/typography/typography';
import React, { useState } from 'react';
import PriceListUpdateSuccessModals from '../modals/price-list-update-success-modals';
import { Card } from 'reactstrap';
import Flex from 'app/components/flex/flex';
import { FORM } from 'app/shared/model/enumerations/form.model';
import Button from 'app/components/button/button';
import { useNavigate } from 'react-router';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import PriceListMMSDetailForm from 'app/modules/price-list/components/price-list-form-mms-detail';

const { UPDATE_QUOTATION_DETAILS } = MUTATION_KEY;

const PriceListUpdateMMSDetail = () => {
  const navigate = useNavigate();

  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  const toogleSuccess = (): void => {
    setIsOpen(prev => !prev);
    navigate(-1);
  };

  const isCreating = useIsMutating({
    mutationKey: [UPDATE_QUOTATION_DETAILS],
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Chi tiết bảng báo giá</Typography>

      <Card className="card-template">
        <Flex direction="column">
          <Typography level={5}>Cập nhập loại hàng MMS</Typography>

          <PriceListMMSDetailForm type="update" toggleSuccess={toggle} />
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
      <PriceListUpdateSuccessModals isOpen={isOpen} toggle={toggle} toogleSuccess={toogleSuccess} />
    </div>
  );
};

export default PriceListUpdateMMSDetail;
