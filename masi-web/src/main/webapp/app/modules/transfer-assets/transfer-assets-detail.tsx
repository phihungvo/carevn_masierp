import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import { PATH } from 'app/constants/path';
import { useNavigate, useParams } from 'react-router';
import TransferAssetsForm from './components/transfer-assets-form';
import useTransferAssets from 'app/hooks/use-transfer-assets';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { useEffect, useState } from 'react';
import { useModalsTransferAssets } from 'app/hooks/use-modals-transfer-assets';
import TransferAssetsUpdateSuccessModals from './modals/transfer-assets-update-success-modals';

const { useGetTransferAssetsByIdQuery } = useTransferAssets;

const TransferAssetsDetail = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const [type , setType] = useState<any>("edit")

  const { data: detail } = useGetTransferAssetsByIdQuery(id);
  const {
    updateSuccess: { openUpdateSuccess, toggleUpdateSuccess },
  } = useModalsTransferAssets();


  useEffect(() => {
    setType(['NEW'].indexOf(detail?.status ?? '') !== -1 ? "edit" : "detail");
  },[detail])


  return (
    <CardV2
      header={
        <Flex justify="space-between" align="center">
          <Typography level={4}>{detail?.code}</Typography>
          <Flex align="center" gap={10}>
            <ButtonV2 onClick={() => navigate(PATH.TRANSFER_ASSETS)}>Đóng</ButtonV2>
            {type === 'edit' ? (
            <ButtonV2
              color="blue"
              variant="solid"
              form={FORM.TRANSFER_ASSETS}
              type="submit"
            >
              Lưu
            </ButtonV2>) : (<></>)}
          </Flex>
        </Flex>
      }
    >
      <TransferAssetsForm type={type} detail={detail} toggleSuccess={toggleUpdateSuccess}
      />

      <TransferAssetsUpdateSuccessModals
        isOpen={openUpdateSuccess}
        toggle={toggleUpdateSuccess}
      />
    </CardV2>
  );
};

export default TransferAssetsDetail;
