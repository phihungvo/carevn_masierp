import { Col, Flex, Row } from 'antd';
import WrapCheckbox from 'app/components/wrap-input-checkbox/WrapCheckbox';
import WrapInputFloat from 'app/components/wrap-input-text/WrapInputFloat';
import PersonSelect from 'app/components/wrap-select/Person';
import Status from 'app/components/wrap-select/Status';
import Unit from 'app/components/wrap-select/Unit';
import WareHouse from 'app/components/wrap-select/Warehouse';
import WrapTextArea from 'app/components/wrap-text-area/WrapTextArea';
import { format2Digit } from 'app/shared/util/format';
import { useFormContext } from 'react-hook-form';
import WrapDate from '../../../../components/wrap-date/WrapDate';
import WrapInputText from '../../../../components/wrap-input-text/WrapInputText';
import WrapSelect from '../../../../components/wrap-select/WrapSelect';
import { GeneralInfoSchemaType } from '../../validations/general-info.validation';

export const GeneralInformation = () => {
  const { watch, getValues, setValue } = useFormContext<GeneralInfoSchemaType>();

  return (
    <Row gutter={[20, 12]} style={{ whiteSpace: 'nowrap' }}>
      <Col span={12} style={{ borderRight: '1px solid #98A2B3 ' }}>
        <Row gutter={[20, 12]}>
          <Col span={12}>
            <Flex vertical gap={12}>
              <WrapInputText<GeneralInfoSchemaType>
                label="Số đăng kí"
                name="generalInformation.registerCode"
              />
              <WrapInputText<GeneralInfoSchemaType>
                label="Số bàn giao"
                name="generalInformation.numberHandover"
              />
              <PersonSelect
                label="Người bàn giao"
                name="generalInformation.handoverPersonId"
              />
              <WrapInputText<GeneralInfoSchemaType>
                label="Số series"
                name="generalInformation.series"
              />
              <WrapInputText<GeneralInfoSchemaType>
                label="Số hóa đơn"
                name="generalInformation.numberBill"
              />
              <WareHouse<GeneralInfoSchemaType>
                label="Kho"
                name="generalInformation.warehouseId"
              />
            </Flex>
          </Col>
          <Col span={12}>
            <Flex vertical gap={12}>
              <WrapDate<GeneralInfoSchemaType>
                label="Ngày đăng kí"
                name="generalInformation.registerDate"
              />
              <WrapDate<GeneralInfoSchemaType>
                label="Ngày bàn giao"
                name="generalInformation.handoverDate"
              />
              <PersonSelect
                label="Người sử dụng"
                name="generalInformation.whoUseId"
                isDisabled
              />
              <WrapSelect<GeneralInfoSchemaType>
                label="Vị trí"
                name="generalInformation.locationId"
                data={{
                  arr: [
                    {
                      label: 'Văn phòng',
                      value: 'OFFICE',
                    },
                    {
                      label: 'Nhà máy',
                      value: 'FACTORY',
                    },
                  ],
                  obj: {
                    OFFICE: {
                      label: 'Văn phòng',
                    },
                    FACTORY: {
                      label: 'Nhà máy',
                    },
                  },
                }}
              />
              <WrapDate<GeneralInfoSchemaType>
                label="Ngày sử dụng"
                name="generalInformation.usingDate"
                picker="date"
              />
              <WrapDate<GeneralInfoSchemaType>
                label="Ngày hóa đơn"
                name="generalInformation.billDate"
                picker="date"
              />
            </Flex>
          </Col>
          <Col span={24}>
            <WrapTextArea<GeneralInfoSchemaType>
              rows={3}
              label="Ghi chú"
              name="generalInformation.note"
            />
          </Col>
        </Row>
      </Col>

      <Col span={12}>
        <Flex vertical gap={12}>
          <Row gutter={[20, 12]}>
            <Col span={12}>
              <Flex vertical gap={12}>
                <Status<GeneralInfoSchemaType>
                  label="Tình trạng"
                  name="generalInformation.status"
                />
                <WrapInputText<GeneralInfoSchemaType>
                  label="Số lượng"
                  name="generalInformation.quantity"
                  disabled
                />
                <Row gutter={12}>
                  <Col span={12}>
                    <WrapInputFloat<GeneralInfoSchemaType>
                      label="Năm sử dụng"
                      name="generalInformation.usageYear"
                      onInputChange={(value) => {
                        setValue('generalInformation.usageMonth', value ? Number(value) * 12 : '' as any);
                      }}
                    />
                  </Col>
                  <Col span={12}>
                    <WrapInputFloat<GeneralInfoSchemaType>
                      label="Tháng sử dụng"
                      name="generalInformation.usageMonth"
                      onInputChange={(value) => {
                        setValue('generalInformation.usageYear', value ? format2Digit(Number(value) / 12) : '' as any);
                      }}
                    />
                  </Col>
                </Row>
                <WrapInputText<GeneralInfoSchemaType>
                  label={
                    <Flex align={'center'} gap={20}>
                      <span>Nhà sản xuất</span>
                      <WrapCheckbox<GeneralInfoSchemaType>
                        label="Trong nước"
                        name="generalInformation.isDomestic"
                        direction='row-reverse'
                      />
                    </Flex>
                  }
                  name="generalInformation.manufacturerId"
                />
              </Flex>
            </Col>
            <Col span={12}>
              <Flex vertical gap={12}>
                <WrapDate<GeneralInfoSchemaType>
                  label="Ngày thanh lý"
                  name="generalInformation.liquidationDate"
                />
                <Unit<GeneralInfoSchemaType>
                  label="Đơn vị tính"
                  name="generalInformation.unitCalculateId"
                  isDisabled
                />
                <WrapDate<GeneralInfoSchemaType>
                  label="Thời hạn bảo hành"
                  name="generalInformation.warrantyPeriod"
                />
                <WrapDate<GeneralInfoSchemaType>
                  label="Ngày sản xuất"
                  name="generalInformation.dateManufacture"
                />
              </Flex>
            </Col>
          </Row>
          <Row gutter={[20, 12]}>
            <Col span={24}>
              <WrapInputText<GeneralInfoSchemaType>
                label="Thông số"
                name="generalInformation.parameter"
              />
            </Col>
          </Row>
          <Row gutter={[20, 12]}>
            <Col span={12}>
              <WrapDate<GeneralInfoSchemaType>
                label="Ngày đình chỉ"
                name="generalInformation.suspensionDay"
              />
            </Col>
            <Col span={12}>
              <WrapInputText<GeneralInfoSchemaType>
                label="Lý do"
                name="generalInformation.reason"
              />
            </Col>
          </Row>
        </Flex>
      </Col>
    </Row>
  );
};
