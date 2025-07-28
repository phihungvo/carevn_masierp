import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { ProductionContext } from '../production-provider';

const ManufactureOrderByStandardUpdateSuccessModal = ({
  directUrl,
}: {
  directUrl?: string;
}) => {
  const navigate = useNavigate();

  const { isOpenUpdateSuccess, toggleUpdateSuccess } =
    useContext(ProductionContext);

  return (
    <Modal
      isOpen={isOpenUpdateSuccess}
      toggle={() => {
        toggleUpdateSuccess();
        if (directUrl) navigate(directUrl);
      }}
      titleHeader="Cập nhật lệnh sản xuất thành công"
      cancel={false}
    >
      <Typography level={4}>
        Bạn đã cập nhật lệnh sản xuất thành công
      </Typography>
    </Modal>
  );
};

export default ManufactureOrderByStandardUpdateSuccessModal;
