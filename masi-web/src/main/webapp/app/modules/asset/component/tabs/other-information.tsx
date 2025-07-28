import { Col, Flex, Row } from 'antd';
import PersonLabel from 'app/components/wrap-input-text/PersonLabel';
import { useFormContext } from 'react-hook-form';
import WrapDate from '../../../../components/wrap-date/WrapDate';
import WrapInputText from '../../../../components/wrap-input-text/WrapInputText';
import { OtherInformationSchemaType } from '../../validations/other-information.validation';

export const OtherInformation = () => {
  const { watch, setValue, reset } = useFormContext<OtherInformationSchemaType>();

  return (
    <Row gutter={20}>
      <Col span={12} style={{ borderRight: '1px solid #98A2B3 ' }}>
        <Row gutter={20}>
          <Col span={12}>
            <Flex vertical gap={12}>
              <PersonLabel<OtherInformationSchemaType>
                label="Người tạo"
                name="otherInformation.createdBy"
                disabled
              />
              <PersonLabel<OtherInformationSchemaType>
                label="Người cập nhật"
                name="otherInformation.updatedBy"
                disabled
              />
            </Flex>
          </Col>
          <Col span={12}>
            <Flex vertical gap={12}>
              <WrapDate<OtherInformationSchemaType>
                label="Ngày tạo"
                name="otherInformation.createdAt"
                disabled
              />
              <WrapDate<OtherInformationSchemaType>
                label="Ngày cập nhật"
                name="otherInformation.updatedAt"
                disabled
              />
            </Flex>
          </Col>
        </Row>
      </Col>
      <Col span={12}>
        <Flex vertical gap={12}>
          <Row gutter={20}>
            <Col span={12}>
              <Flex vertical gap={12}>
                <WrapInputText<OtherInformationSchemaType>
                  label="Software"
                  name="otherInformation.software"
                />
              </Flex>
            </Col>
            <Col span={12}>
              <Flex vertical gap={12}>
                <WrapInputText<OtherInformationSchemaType>
                  label="Nhân viên PX"
                  name="otherInformation.pxEmployee"
                />
              </Flex>
            </Col>
          </Row>
          <Row>
            <Col span={24}>
              <WrapInputText<OtherInformationSchemaType>
                type="textarea"
                label="Thông tin PX"
                name="otherInformation.pxInformation"
              />
            </Col>
          </Row>
        </Flex>
      </Col>
    </Row>
  );
};
