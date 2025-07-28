import FormSelect from 'app/components/form/form-select';
import { Typography } from 'app/components/typography/typography';
import useWorkspace from 'app/hooks/use-workspace';
import { INVENTORIES_STATUS } from 'app/shared/model/inventories-storage.model';
import { InventoriesExportSchema } from 'app/validation/inventories-export.validation';
import { useFormContext } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { useGetWorkspacesQuery } = useWorkspace;

export const InventoriesExportReceive = () => {
  const { control, watch } = useFormContext<InventoriesExportSchema>();

  const { data: workspaces } = useGetWorkspacesQuery();

  const disabled =
    watch('status') === (INVENTORIES_STATUS.WAITING_APPROVED as string) ||
    watch('status') === (INVENTORIES_STATUS.CANCELLED as string) ||
    watch('status') === (INVENTORIES_STATUS.APPROVED as string);

  return (
    <>
      <Typography level={5}>Nơi nhận</Typography>
      <Row>
        <Col md={3}>
          <FormSelect
            control={control}
            name="workspaceId"
            label="Nơi nhận"
            placeholder="Nơi nhận"
            options={workspaces?.data?.map(c => ({
              label: `${c?.name || ''}`,
              value: c?.id,
            }))}
            disabled={disabled}
          />
        </Col>
      </Row>
    </>
  );
};
