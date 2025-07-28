import Flex from 'app/components/flex/flex';
import FormSelect from 'app/components/form/form-select';
import FormDatePickerV2 from 'app/components/formV2/form-date/form-date-picker';
import FormInputV2 from 'app/components/formV2/form-input/form-input';
import { Typography } from 'app/components/typography/typography';
import {
  DEFAULT_INTEGER_REGEX,
  DEFAULT_PAGE_SIZE_NAX,
} from 'app/constants/common';
import useCustomers from 'app/hooks/use-customers';
import useEmployee from 'app/hooks/use-employee';
import useInventoriesStorage from 'app/hooks/use-inventories-storage';
import useItems from 'app/hooks/use-items';
import useProductionPackage from 'app/hooks/use-production-package';
import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';
import { PRODUCTION_QUALITY_STATUS } from 'app/shared/model/enumerations/production-quality-control.model';
import { formatDecimalPrecision } from 'app/shared/util/decimal-precision';
import { handleValidatePaste } from 'app/shared/util/handle-valid-decimal';
import { ProductionQualityFormSchema } from 'app/validation/production-quality.validation';
import { useFormContext } from 'react-hook-form';
import { useParams } from 'react-router';
import { Col, Row } from 'reactstrap';

const { useGetEmployeesQuery } = useEmployee;
const { useProductionPackages } = useProductionPackage;
const { useGetEnabledCustomers } = useCustomers;
const { useGetItemsPercentProteinQuery } = useItems;

const ProductionQualityForm = () => {
  const { id } = useParams();

  const methods = useFormContext<ProductionQualityFormSchema>();
  const { control, formState, setValue, watch } = methods;
  const statusWatch = watch('status');
  const statusMWatch = watch('statusM');

  const { data: employees, isLoading } = useGetEmployeesQuery();
  const { data: packages } = useProductionPackages({
    size: DEFAULT_PAGE_SIZE_NAX,
    isHasQC: !id ? false : undefined,
  });
  const { data: customers, isLoading: cusLoading } = useGetEnabledCustomers();
  // const { data: depreciations } = useGetItemsPercentProteinQuery({
  //   size: DEFAULT_PAGE_SIZE_NAX,
  // });

  const disabled =
    statusMWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string) ||
    statusWatch === (PRODUCTION_QUALITY_STATUS.DISPOSED as string) ||
    statusWatch === (PRODUCTION_QUALITY_STATUS.REJECTED as string) ||
    Boolean(watch('attributes.isDone'));

  return (
    <Flex direction="column" gap={16}>
      <Typography level="paragraph" className="bold test-check-heading">
        Thông tin chung:
      </Typography>

      <Row>
        <Col md={4}>
          <FormInputV2
            control={control}
            name="sampleNo"
            label="Mã mẫu"
            disabled={disabled}
          />
        </Col>

        <Col md={4}>
          <FormDatePickerV2
            setValue={setValue}
            control={control}
            label="Ngày lấy mẫu"
            name="samplingDate"
            formState={formState}
            disabled={disabled}
          />
        </Col>

        <Col md={4}>
          <FormInputV2
            control={control}
            name="productType"
            label="Loại hàng"
            disabled={disabled}
          />
        </Col>

        <Col md={4}>
          <FormInputV2
            control={control}
            name="sampleWeight"
            label="Số lượng mẫu/ khối lượng"
            onChange={e =>
              setValue(
                'sampleWeight',
                formatDecimalPrecision(
                  Number(e?.target?.value?.toString()?.replace(/,/g, '')),
                ),
              )
            }
            onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
            disabled={disabled}
          />
        </Col>

        <Col md={4}>
          <FormSelect
            control={control}
            id="customer"
            name="customer"
            placeholder="Chọn khách hàng"
            label="Khách hàng"
            options={customers?.data?.map(c => ({
              label: c?.companyName,
              value: c?.id,
            }))}
            isLoading={cusLoading}
            disabled={disabled}
          />
        </Col>

        <Col md={4}>
          <FormDatePickerV2
            setValue={setValue}
            control={control}
            label="Ngày xả mẫu"
            name="sampleReleaseDate"
            formState={formState}
            disabled={disabled}
          />
        </Col>

        <Col md={4}>
          <FormSelect
            control={control}
            id="samplingEmployeeId"
            name="samplingEmployeeId"
            placeholder="Chọn người lưu mẫu"
            label="Người lưu mẫu"
            options={employees?.data?.map(e => ({
              label: `${e?.code || ''} - ${e?.employeeProfile.fullName || ''}`,
              value: e?.id,
            }))}
            isLoading={isLoading}
            disabled={disabled}
          />
        </Col>

        <Col md={4}>
          <FormSelect
            control={control}
            id="packageId"
            name="packageId"
            placeholder="Chọn mã đóng gói"
            label="Mã đóng gói"
            options={packages?.data?.map(i => ({
              label: i?.packageCode,
              value: i?.id,
            }))}
            onChanges={e => {
              const selected = packages?.data?.find(x => x.id === e);
              if (selected)
                setValue('manufactureOrderId', selected?.manufactureOrderId);
            }}
            isLoading={isLoading}
            disabled={disabled}
          />
        </Col>

        <Col md={12}>
          <FormInputV2
            control={control}
            name="reason"
            label="Lý do"
            disabled={disabled}
          />
        </Col>
      </Row>

      <Flex style={{ border: '1px solid #E4E7EC', borderRadius: '6px' }}>
        <Row style={{ margin: '16px', width: '100%' }}>
          <Col md={8} style={{ borderRight: '1px solid #d3d5d7' }}>
            <Flex direction="column" gap={16}>
              <Typography level="paragraph" className="bold test-check-heading">
                Nội bộ:
              </Typography>

              <Row>
                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="internalHum"
                    label="Độ ẩm"
                    disabled={disabled}
                  />
                </Col>
                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="internalTvn"
                    label="TVN"
                    disabled={disabled}
                  />
                </Col>
                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="internalAsh"
                    label="Tro"
                    disabled={disabled}
                  />
                </Col>
                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="internalProtein"
                    label="Protein"
                    disabled={disabled}
                  />
                </Col>
              </Row>
            </Flex>
            <Flex direction="column" gap={16}>
              <Typography level="paragraph" className="bold test-check-heading">
                Đối tác:
              </Typography>

              <Row>
                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="externalHum"
                    label="Độ ẩm"
                    disabled={disabled}
                  />
                </Col>

                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="externalTvn"
                    label="TVN"
                    disabled={disabled}
                  />
                </Col>
                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="externalAsh"
                    label="Tro"
                    disabled={disabled}
                  />
                </Col>
                <Col md={6}>
                  <FormInputV2
                    control={control}
                    name="externalProtein"
                    label="Protein"
                    disabled={disabled}
                  />
                </Col>
              </Row>
            </Flex>
          </Col>
          <Col md={4} style={{ margin: 'auto' }}>
            {/* <FormSelect
              control={control}
              name="itemId"
              label="% đạm áp dụng"
              placeholder="Vui lòng chọn % đạm áp dụng"
              options={depreciations?.data?.map(x => ({
                value: x?.id,
                label: `${x.percentProtein}`,
              }))}
              onChanges={e => {
                const selected = depreciations?.data?.find(x => x.id === e);
                if (selected)
                  setValue(
                    'proteinPercentageApply',
                    `${selected?.percentProtein}`,
                  );
              }}
              disabled={disabled}
            /> */}
            <FormInputV2
              control={control}
              name="proteinPercentageApply"
              label="% đạm áp dụng"
              placeholder="Vui lòng nhập % đạm áp dụng"
              disabled={disabled}
              onPaste={e => handleValidatePaste(e, DEFAULT_INTEGER_REGEX)}
            />
          </Col>
        </Row>
      </Flex>
    </Flex>
  );
};

export default ProductionQualityForm;
