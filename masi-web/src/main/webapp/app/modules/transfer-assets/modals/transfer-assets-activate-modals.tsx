import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import useTransferAssets from 'app/hooks/use-transfer-assets';

const { useEnableTransferAssetsMutation } = useTransferAssets;

interface ITransferAssetsActivateModalsProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
}

const TransferAssetsActivateModals = (props: ITransferAssetsActivateModalsProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const { mutate, isPending } = useEnableTransferAssetsMutation();

  const onOk = () => {
    mutate(selectedRecord, {
      onSuccess: () => {
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
      titleHeader="Hoàn thành diều chuyển"
    >
      <Typography level={4}>Bạn muốn hoàn thành điều chuyển này?</Typography>
    </Modal>
  );
};

export default TransferAssetsActivateModals;
