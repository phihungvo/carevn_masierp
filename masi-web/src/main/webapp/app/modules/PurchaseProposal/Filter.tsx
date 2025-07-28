import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import Modal from 'app/components/modal/modal';
import { DATE_FORMAT } from 'app/constants/common';
import useSuppliesRequest from 'app/hooks/use-supplies-request';
import { suppliesStatusArr } from 'app/shared/model/enumerations/supplies-request';
import { ISuppliesRequestParams } from 'app/shared/model/supplies-request.model';
import dayjs from 'dayjs';
import { useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { useGetSuppliesRequestsType } = useSuppliesRequest;

type Props = {
  isOpen: boolean;
  toggle: () => void;
  setQuery: React.Dispatch<React.SetStateAction<ISuppliesRequestParams>>;
  query: ISuppliesRequestParams;
};

const Filter = (props: Props) => {
  const { isOpen, toggle, setQuery } = props;

  const { control, handleSubmit, reset, setValue } = useForm();

  const supplies_type_query = useGetSuppliesRequestsType(
    { page: 0, size: 2000000 },
    {
      select: res => {
        return res?.['data']?.data.map(item => ({
          label: item.name,
          value: item.id,
        }));
      },
    },
  );

  const onSubmit = (data: any) => {
    let tmp = {};
    if (data?.requestDate?.[0]) {
      tmp['requestDate.greaterThanOrEqual'] = dayjs(
        data?.requestDate?.[0]?.toDate(),
      ).format(DATE_FORMAT.YEAR_DATE);
    }
    if (data?.requestDate?.[1]) {
      tmp['requestDate.lessThanOrEqual'] = dayjs(
        data?.requestDate?.[1]?.toDate(),
      ).format(DATE_FORMAT.YEAR_DATE);
    }
    if (data?.requestTypeId) {
      tmp['requestTypeId.equals'] = data?.requestTypeId;
    }
    if (data?.requestStatus) {
      tmp['requestStatus.equals'] = data?.requestStatus;
    }
    setQuery(pre => ({
      ...pre,
      ...tmp,
    }));
    toggle();
  };

  const onCancel = () => {
    setQuery(pre => {
      let tmp = {
        size: pre?.size,
        page: pre?.page,
      }
      return { ...tmp }
    });
    reset({
      'requestDate': null,
      'requestTypeId': null,
      'requestStatus': null,
    });
    toggle();
  }

  return (
    <Modal
      isOpen={isOpen}
      title="Bộ lọc"
      okText="Áp dụng"
      cancelText="Đặt lại"
      toggle={toggle}
      onOk={handleSubmit(onSubmit)}
      onCancel={onCancel}
    >
      <form>
        <Row>
          <Col md={12}>
            <FormDatePickerV2
              control={control}
              label="Thời gian"
              name="requestDate"
              range
              setValue={setValue}
            />
          </Col>
        </Row>
        <Row>
          <Col md={6}>
            <FormSelect
              control={control}
              label="Loại đề xuất"
              name="requestTypeId"
              options={supplies_type_query?.data || []}
              isClearable
              isSearchable
              placeholder="Chọn"
            />
          </Col>
          <Col md={6}>
            <FormSelect
              control={control}
              label="Trạng thái"
              name="requestStatus"
              options={suppliesStatusArr}
              isClearable
              isSearchable
              placeholder="Chọn"
            />
          </Col>
        </Row>
      </form>
    </Modal>
  );
};

export default Filter;
