import AttachmentPreviewV2 from 'app/components/attachment-preview-v2/attachment-preview';
import ButtonAdd from 'app/components/ButtonV2/ButtonAdd';
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete';
import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import TableV2, { TableColumns } from 'app/components/table-v2/Table';
import { DATE_FORMAT, FILE_UTIL } from 'app/constants/common';
import useFile from 'app/hooks/use-file';
import { IFIle } from 'app/shared/model/file.model';
import { SupplierSchema } from 'app/validation/supplier.validation';
import dayjs from 'dayjs';
import React, { useRef } from 'react';
import { useFormContext } from 'react-hook-form';

const { usePostFile } = useFile;

const SupplierAttachment = () => {
  const methods = useFormContext<SupplierSchema>();
  const { setValue, watch } = methods;
  const watchAttachments = watch('attachment');

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
                'attachment',
                newArr?.map(x => ({ ...x, createdAt: new Date(x.createdAt) })),
              );
            }}
          />
        ),
      },
    },
  ];

  const onChangeFile = (file: IFIle) => {
    if (file) {
      setValue('attachment', [
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
        />
      </div>

      <TableV2<any>
        table_id="file"
        columns={columns}
        data={[...(watchAttachments ?? [])]}
        className={{ table: 'at__table' }}
      />
    </Flex>
  );
};

export default SupplierAttachment;
