import AttachmentPreviewV2 from 'app/components/attachment-preview-v2/attachment-preview';
import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useFile from 'app/hooks/use-file';
import { PAYMENT_REQUEST_STATUS } from 'app/shared/model/enumerations/payment-request';
import { IFIle } from 'app/shared/model/file.model';
import dayjs from 'dayjs';
import { useRef } from 'react';
import { useFormContext } from 'react-hook-form';
import './Attachments.scss';

const { usePostFile } = useFile;

interface Props {
  payload_key?: string;
}

const Attachments = (props: Props) => {
  const { payload_key } = props;
  const methods = useFormContext();
  const { setValue, watch } = methods;
  const watchAttachments = watch(payload_key);

  const disabled =
    watch('status') === (PAYMENT_REQUEST_STATUS.WAITING_APPROVE as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.CANCELLED as string) ||
    watch('status') === (PAYMENT_REQUEST_STATUS.APPROVED as string);

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
      setValue(payload_key, [
        ...(watchAttachments ?? []),
        {
          fileId: file.id,
          fileName: file.name,
          createdAt: new Date(),
          filePath: file?.path,
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
        table_id="attachments"
        columns={columns}
        data={[...(watchAttachments ?? [])]}
        className={{ table: 'at__table' }}
      />
    </Flex>
  );
};

Attachments.defaultProps = {
  payload_key: 'attachments',
};

export default Attachments;
