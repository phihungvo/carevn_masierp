import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';
import AdvanceInfo from './AdvanceInfo/AdvanceInfo';
import useInventoriesStorage from 'app/hooks/use-inventories-storage';
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE_NAX } from 'app/constants/common';
import FormSelect from 'app/components/form/form-select';
import machineryEquipmentMapping from '../machinery-equipment-mapping';
import HistoryInfo from './HistoryInfo/HistoryInfo';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
const { machineryEquipmentStatusMap, maintenanceCycleList, machineryEquipmentLocationMap } = machineryEquipmentMapping;

const { useGetInventoriesStorageQuery } = useInventoriesStorage;

const MachineryEquipmentGeneralForm = (props: any) => {

  const { data } = props;
  const methods = useFormContext();
  const { control, formState, setValue } = methods

  const { data: listItem } = useGetInventoriesStorageQuery({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE_NAX,
    'id.notEquals': data?.id
  })

  return (<>
    <Row>
      <Col md={6} style={{
        borderRight: "1px solid #bfc1c5",
      }}>
        <Row >
          <Col md={6}>
            <FormInputV2
              control={control}
              id="code"
              name="code"
              label="Mã thiết bị"
              disabled
            />
          </Col>
          <Col md={6}>
            <FormInputV2
              control={control}
              id="item.name"
              name="item.name"
              label="Tên tài sản"
              disabled
            />
          </Col>
          <Col md={6}>
            <FormSelect
              control={control}
              id="attribute.general.linkDeviceId"
              name="attribute.general.linkDeviceId"
              label="Liên kết thiết bị"
              options={listItem?.data?.map(s => ({
                value: s?.id,
                label: `${s.code} - ${s?.item?.name || ''}`,
              }))}
              placeholder="Vui lòng chọn"
            />
          </Col>
          <Col md={6}>
            <FormInputV2
              control={control}
              id="attribute.general.symbol"
              name="attribute.general.symbol"
              label="Ký hiệu"
              placeholder="Điền"
            />
          </Col>
          <Col md={6}>
            <FormInputV2
              control={control}
              id="item.itemCategory.name"
              name="item.itemCategory.name"
              label="Loại thiết bị"
              disabled
            />
          </Col>
          <Col md={6}>
            <FormSelect
              control={control}
              id="attribute.general.status"
              name="attribute.general.status"
              label="Trạng thái"
              options={machineryEquipmentStatusMap?.map(s => ({
                value: s?.value,
                label: `${s.label}`,
              }))}
              placeholder="Vui lòng chọn"
            />
          </Col>

          <Col md={6}>
          <FormSelect
              control={control}
              id="attribute.general.location"
              name="attribute.general.location"
              label="Vị trí"
              options={machineryEquipmentLocationMap?.map(s => ({
                value: s?.value,
                label: `${s.label}`,
              }))}
              placeholder="Vui lòng chọn"
            />
          </Col>
        </Row>
      </Col>
      <Col md={6} >
        <Row>
          <Col md={6}>
            <FormInputV2
              control={control}
              id="attribute.general.countryMade"
              name="attribute.general.countryMade"
              label="Nước sản xuất"
              placeholder="Điền"
            />
          </Col>
          <Col md={6}>
            <FormInputV2
              control={control}
              id="supplier.name"
              name="supplier.name"
              label="Nhà cung cấp"
              disabled
            />
          </Col>
          <Col md={6}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              name="attribute.general.installationDate"
              label="Ngày lắp đặt"
              placeholder="Chọn ngày"
              setValue={setValue}
            />
          </Col>
          <Col md={6}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              name="attribute.general.operationDate"
              label="Ngày vận hành"
              placeholder="Chọn ngày"
              setValue={setValue}
            />
          </Col>
          <Col md={6} style={{
            display: "inline-flex",
          }}>
            <Col md={6} style={{
              paddingRight: "calc((var(--bs-gutter-x) * 0.5)/2)",
            }}>
              <FormInputV2
                control={control}
                name="item.uom.name"
                label="Đơn vị tính"
                placeholder="Điền"
                disabled
              />
            </Col>
            <Col md={6} style={{
              paddingLeft: "calc((var(--bs-gutter-x) * 0.5)/2)",
            }}>
              <FormInputV2
                control={control}
                name="quantity"
                label="Số lượng"
                placeholder="Điền"
                disabled
              />
            </Col>
          </Col>
          <Col md={6}>
            <FormInputV2
              control={control}
              name="price"
              label="Nguyên giá"
              placeholder="Điền"
              disabled
            />
          </Col>
          <Col md={6}>
             <FormSelect
                control={control}
                id="attribute.general.maintenanceCycle"
                name="attribute.general.maintenanceCycle"
                label="Chu kỳ bảo trì"
                options={maintenanceCycleList?.map(s => ({
                  value: s?.value,
                  label: `${s.label}`,
                }))}
                placeholder="Vui lòng chọn"
                />
          </Col>
          <Col md={6}>
            <FormDatePickerV2
              control={control}
              formState={formState}
              name="attribute.general.beginDate"
              label="Bắt đầu từ ngày"
              placeholder="Chọn ngày"
              setValue={setValue}
            />
          </Col>
        </Row>
      </Col>
    </Row>
    <Row>
      <Col md={12}>
        <FormInputV2
          control={control}
          id="attribute.general.note"
          name="attribute.general.note"
          label="Ghi chú"
          placeholder="Điền"
        />
      </Col>
      <Col md={12}>
        <FormInputV2
          control={control}
          id="attribute.general.note_other_1"
          name="attribute.general.note_other_1"
          label="Ghi chú khác 1"
          placeholder="Điền"
        />
      </Col>
      <Col md={12}>
        <FormInputV2
          control={control}
          id="attribute.general.note_other_2"
          name="attribute.general.note_other_2"
          label="Ghi chú khác 2"
          placeholder="Điền"
        />
      </Col>
    </Row>
    <Row>
      <HistoryInfo name="attribute.general.history" />
    </Row>
  </>)
};

export default MachineryEquipmentGeneralForm;
