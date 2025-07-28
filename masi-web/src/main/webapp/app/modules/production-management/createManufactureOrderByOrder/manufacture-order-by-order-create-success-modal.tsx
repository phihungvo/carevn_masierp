import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { ProductionContext } from '../production-provider';
import { useNavigate } from 'react-router';

const ManufactureOrderByOrderCreateSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const { isOpenCreateSuccess, toggleCreateSuccess } =
    useContext(ProductionContext);

  return (
    <Modal
      isOpen={isOpenCreateSuccess}
      toggle={() => {
        toggleCreateSuccess();
        if (directUrl) navigate(directUrl);
      }}
      titleHeader="Tạo mới lệnh sản xuất thành công"
      cancel={false}
    >
      <Typography level={4}>Bạn đã tạo mới lệnh sản xuất thành công</Typography>
    </Modal>
  );
};

export default ManufactureOrderByOrderCreateSuccessModal;
