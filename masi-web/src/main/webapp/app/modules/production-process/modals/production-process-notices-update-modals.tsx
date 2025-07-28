import React from 'react';

import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';

interface IProductionProcessNoticesUpdateModals {
  isOpen: boolean;
  toggle: () => void;
}

const ProductionProcessNoticesUpdateModals = (props: IProductionProcessNoticesUpdateModals) => {
  const { isOpen, toggle } = props;

  return (
    <Modal isOpen={isOpen} toggle={toggle} className="production-process-notices-update-modals" cancel={false}>
      <Typography level={3}>Vui lòng cập nhật</Typography>
      <Typography level={4}>Bạn phải cập nhật thì mới có thể bắt đầu công đoạn</Typography>
    </Modal>
  );
};

export default ProductionProcessNoticesUpdateModals;
