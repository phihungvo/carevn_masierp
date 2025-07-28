import { Card } from 'reactstrap';
import React, { ReactElement, useState } from 'react';
import { NavigateFunction, useNavigate } from 'react-router';

import Flex from 'app/components/flex/flex';
import Button from 'app/components/button/button';
import PriceListFormKimLongDetail from 'app/modules/price-list/components/price-list-form-kim-long-detail';
import { useIsMutating } from '@tanstack/react-query';
import { MUTATION_KEY } from 'app/constants/query-key';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { Typography } from 'app/components/typography/typography';
import PriceListUpdateSuccessModals from 'app/modules/price-list/modals/price-list-update-success-modals';

const { CREATE_QUOTATION_DETAILS } = MUTATION_KEY;

const PriceListCreateKimLongDetail = (): ReactElement => {
  const navigate: NavigateFunction = useNavigate();

  const [isOpen, setIsOpen] = useState(false);

  const toggle = (): void => {
    setIsOpen(prev => !prev);
  };

  const toogleSuccess = (): void => {
    setIsOpen(prev => !prev);
    navigate(-1);
  };

  const isCreating = useIsMutating({
    mutationKey: [CREATE_QUOTATION_DETAILS],
  });

  return (
    <div className='page_container'>
      <Typography level={4}>Chi tiết bảng báo giá</Typography>
      <Card className="card-template">
        <Flex direction="column">
          <Typography level={5}>Cập nhập loại hàng kim long</Typography>

          <PriceListFormKimLongDetail type="update" toggleSuccess={toggle} />
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

export default PriceListCreateKimLongDetail;
