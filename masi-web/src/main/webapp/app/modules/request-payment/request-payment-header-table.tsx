import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { useDownloadXlsx } from 'app/hooks/use-download';
import usePaymentRequest from 'app/hooks/use-payment-request';

interface IRequestPaymentHeaderTableProps {
  search?: string;
  handleSearch?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  toggleFilter: () => void;
}

const icon_path = 'content/images/vuesax/linear/';
const { useGetUniformExportReportExcel } = usePaymentRequest;

function RequestPaymentHeaderTable(props: IRequestPaymentHeaderTableProps) {
  const { search, handleSearch, toggleFilter } = props;

  const { trigger, data: dataFile } = useGetUniformExportReportExcel();

  const onExportRequestPaymentData = () => trigger();

  // hook download xlsx
  useDownloadXlsx(dataFile?.data, `DNTT-DNTU-DNHU`, 'xlsx');

  return (
    <Flex className="rp__header" justify="space-between" align="center">
      <InputSearch value={search} onChange={handleSearch} />
      <Flex gap={16}>
        <ButtonV2
          left_section={
            <img src={icon_path + 'three-line-filter.svg'} alt="filter" />
          }
          onClick={() => toggleFilter()}
        >
          Bộ lọc
        </ButtonV2>
        <AuthGuard permissionKey='REQUEST_PAYMENT.EXPORT'>
          <ButtonV2
            left_section={
              <img src={icon_path + 'export-excel.svg'} alt="filter" />
            }
            onClick={() => onExportRequestPaymentData()}
          >
            Excel
          </ButtonV2>
        </AuthGuard>
      </Flex>
    </Flex>
  );
}

export default RequestPaymentHeaderTable;
