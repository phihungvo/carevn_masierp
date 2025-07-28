import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useTransferAssets from 'app/hooks/use-transfer-assets';

const {   useDisableTransferAssetsMutation } = useTransferAssets;

interface ITransferAssetsDisposeModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const TransferAssetsDisposeModals = (props: ITransferAssetsDisposeModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useDisableTransferAssetsMutation();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess() {
        toggle();
        toggleSuccess();
      },
    });
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-dispose-transfer-assets-success"
      okText="Xác nhận"
      onOk={onOk}
      isabledOk={isPending}
      titleHeader="Huỷ điều chuyển"
    >
      <Typography level={4}>Bạn muốn huỷ điều chuyển này?</Typography>
    </Modal>
  );
};

export default TransferAssetsDisposeModals;
