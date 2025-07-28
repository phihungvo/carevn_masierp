import AttachmentPreviewV2 from 'app/components/attachment-preview-v2/attachment-preview';
import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useFile from 'app/hooks/use-file';
import { IFIle } from 'app/shared/model/file.model';
import { STOCKTAKING_STATUS } from 'app/shared/model/stocktaking.model';
import { StocktakingSchema } from 'app/validation/stocktaking.validation';
import dayjs from 'dayjs';
import { useRef } from 'react';
import { useFormContext } from 'react-hook-form';
import './style.scss';
import FormError from 'app/components/form/form-error';

const { usePostFile } = useFile;

const StocktakingAttachments = () => {
  const methods = useFormContext<StocktakingSchema>();
  const { setValue, watch, formState } = methods;
  const watchAttachments = watch('attachments');

  const disabled =
    watch('status') === (STOCKTAKING_STATUS.WAITING_APPROVED as string) ||
    watch('status') === (STOCKTAKING_STATUS.CANCELLED as string) ||
    watch('status') === (STOCKTAKING_STATUS.APPROVED as string) ||
    watch('status') === (STOCKTAKING_STATUS.COMPLETED as string);

  const columns: TableColumns<any> = [
    {
      header: { render: 'Tên tệp' },
      body: {
        render: ({ data }) => (
          <AttachmentPreviewV2
            fileUrl={FILE_UTIL + '/' + data?.fileId}
            name={data?.fileName}
          />
        ),
      },
    },
    {
      header: { render: 'Ngày scan' },
      body: {
        render: ({ data }) => dayjs(data?.createdAt).format(DATE_FORMAT.DATE),
      },
    },
    {
      header: { render: <></> },
      body: {
        render: ({ index }) => (
          <ButtonDelete
            onClick={() => {
              const newArr = JSON.parse(
                JSON.stringify(watchAttachments),
              ).filter((_x, idx) => idx !== index);
              setValue(
                'attachments',
                newArr?.map(x => ({ ...x, createdAt: new Date(x.createdAt) })),
              );
            }}
            disabled={disabled}
          />
        ),
      },
    },
  ];

  const onChangeFile = (file: IFIle) => {
    if (file) {
      setValue('attachments', [
        ...(watchAttachments ?? []),
        {
          fileId: file.id,
          fileName: file.name,
          createdAt: new Date(),
        },
      ]);
    }
  };

  const fileInputRef = useRef<HTMLInputElement>(null);
  const { mutate: uploadFile } = usePostFile(onChangeFile);

  return (
    <Flex direction="column" gap={8} className="at">
      <div className="at_header">
        <span>Tệp đính kèm</span>

        <ButtonAdd
          text={
            <>
              Thêm tệp
              <InputFile
                onFileChange={file => uploadFile(file)}
                name="fileAttachment"
                hidden
                ref={fileInputRef}
              />
            </>
          }
          onClick={() => fileInputRef.current?.click()}
          disabled={disabled}
        />
      </div>

      <TableV2<any>
        table_id="file"
        columns={columns}
        data={[...(watchAttachments ?? [])]}
        className={{ table: 'at__table' }}
      />

      {(formState.errors?.attachments?.message ||
        formState.errors?.attachments?.root) && (
        <FormError
          message={
            formState.errors?.attachments?.message ||
            formState.errors?.attachments?.root?.message
          }
        />
      )}
    </Flex>
  );
};

StocktakingAttachments.defaultProps = {
  payload_key: 'file',
};

export default StocktakingAttachments;
