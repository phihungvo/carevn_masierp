import { CardSubtitle } from 'reactstrap';
import { useNavigate } from 'react-router';
import React, { ReactElement } from 'react';
import { useParams } from 'react-router-dom';
import Button from 'app/components/button/button';
import useQuotation from 'app/hooks/use-quotations';
import ButtonIcon from 'app/components/button-icon/button-icon';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useDownloadPdf } from 'app/hooks/use-download';
import { COMPANY } from 'app/shared/model/enumerations/company.model';
import { setIsUpdate } from 'app/shared/reducers/quotation';

interface IPriceListDetailHeaderProps {
  name: string;
  toggleDownloadSuccessful: () => void;
}

const { useGetQuotationExport } = useQuotation;

const PriceListDetailHeader = (props: IPriceListDetailHeaderProps): ReactElement => {
  const { name, toggleDownloadSuccessful } = props;

  const { id } = useParams();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);

  const { trigger, data } = useGetQuotationExport(id, true, toggleDownloadSuccessful);

  const handleDownload = () => {
    trigger();
  };

  const handleGoBack = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    dispatch(setIsUpdate(false));
    navigate(-1);
  };

  useDownloadPdf(data?.data, name, 'pdf');

  return (
    <div className="card-header-container">
      <CardSubtitle className="card-header-subtitle">
        Báo giá: {account?.company?.normalizedName === COMPANY.KIM_LONG ? 'Kim Long' : 'MMS'}
      </CardSubtitle>
      <div className="card-header-extra">
        <Button color="primary" onClick={e => handleGoBack(e)}>
          Quay lại
        </Button>
        <ButtonIcon
          onClick={() => handleDownload()}
          icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
        />
      </div>
    </div>
  );
};

export default PriceListDetailHeader;
