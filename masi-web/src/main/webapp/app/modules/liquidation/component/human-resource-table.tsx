import { Col, Flex, Row, Typography } from "antd";
import ButtonAdd from "app/components/ButtonV2/ButtonAdd";
import ButtonDelete from "app/components/ButtonV2/ButtonDelete";
import TableV2, { TableColumns } from "app/components/table-v2/Table";
import WrapInputText from "app/components/wrap-input-text/WrapInputText";
import PersonSelect from "app/components/wrap-select/Person";
import { cloneDeep, uniqueId } from "lodash";
import { useFormContext } from "react-hook-form";
import { HumanResourceItem, HumanResourceSchemaType } from "../validations/human-resource-table.validation";
import FormError from "app/components/form/form-error";

type Props = {}

const HumanResourceTable = (props: Props) => {

    const {
        watch, setValue, getValues, formState: { errors }
    } = useFormContext<HumanResourceSchemaType>()
    let humanResouces = watch('humanResource')

    const onAdd = () => {
        humanResouces = humanResouces || []
        humanResouces.push({
            humanId: uniqueId('human-'),
        })
        setValue('humanResource', JSON.parse(JSON.stringify(humanResouces)))
    }

    const onRemove = (index: number) => () => {
        let filtered = humanResouces.filter((_: any, i: number) => i !== index)
        setValue('humanResource', cloneDeep(filtered))
    }

    const columns: TableColumns<HumanResourceItem> = [
      {
        header: {
          render: 'Nhân viên',
        },
        body: {
          render: ({ data, index }) => (
            <PersonSelect<HumanResourceSchemaType>
                key={data?.humanId}
                name={`humanResource.${index}.employeeId`}
            />
          ),
        },
      },
      {
        header: {
          render: 'Chức vụ',
        },
        body: {
          render: ({ data, index }) => (
            <WrapInputText<HumanResourceSchemaType>
                key={data?.humanId}
                name={`humanResource.${index}.position`}
            />
          ),
        },
      },
      {
        header: {
          render: 'Đại diện',
        },
        body: {
          render: ({ data, index }) => (
            <WrapInputText<HumanResourceSchemaType>
                key={data?.humanId}
                name={`humanResource.${index}.representative`}
            />
          ),
        },
      },
      {
        header: {
          render: 'Vai trò',
        },
        body: {
          render: ({ data, index }) => (
            <WrapInputText<HumanResourceSchemaType>
                key={data?.humanId}
                name={`humanResource.${index}.role`}
            />
          ),
        },
      },
      {
        header: {
          render: '',
        },
        body: {
          render: ({ data, index }) => (
            <ButtonDelete onClick={onRemove(index)} />
          ),
        },
      },
    ];

  return (
    <Row gutter={[16, 12]}>
      <Col span={24}>
        <Flex gap={24}>
          <Typography.Text>Nhân sự</Typography.Text>
          <ButtonAdd text="Thêm" onClick={onAdd} />
        </Flex>
      </Col>
      <Col span={24}>
        <TableV2<HumanResourceItem>
          table_id="human-resource-table"
          columns={columns}
          data={getValues('humanResource')}
          rowKey="employeeeId"
        />
      </Col>
      {errors?.humanResource && (
        <Col span={24}>
          <FormError message={errors?.humanResource?.message} />
        </Col>
      )}
    </Row>
  );
}

export default HumanResourceTable