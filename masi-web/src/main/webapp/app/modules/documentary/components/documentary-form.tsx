import { zodResolver } from '@hookform/resolvers/zod';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import InputFile from 'app/components/input/input-file';
import useDocumentary from 'app/hooks/use-documentary';
import useEmployee from 'app/hooks/use-employee';
import { DOCUMENTARY_GROUP, DOCUMENTARY_TYPE } from 'app/shared/model/enumerations/documentary';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { documentarySchema, DocumentarySchema } from 'app/validation/documentary.validation';
import React, { useEffect, useRef, useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { DateObject } from 'react-multi-date-picker';
import { Col, Row } from 'reactstrap';
import documentaryMapping from '../documentary-mapping';
import FormSelect from 'app/components/form/form-select';
import useFile from 'app/hooks/use-file';
import { IBodyFile, IFIle } from 'app/shared/model/file.model';
import { FILE_UTIL } from 'app/constants/common';
import Card from 'app/components/card/card';

const { documentaryGroupMapping, documentaryTypeMapping } = documentaryMapping;

const { useGetEmployeeProfilesQuery } = useEmployee;
const { useGetDocumentaryByIdQuery, usePostDocumentaryMutation, usePatchDocumentaryMutation } = useDocumentary;
const { usePostFile } = useFile;

interface IDocumentaryFormProps {
  type: 'create' | 'update';
  toggle?: () => void;
  toggleSuccess?: () => void;
  selectedRecord?: string | null;
  setSelectedRecord?: (value: string | null) => void;
  setSelectedRowKeys?: React.Dispatch<React.SetStateAction<string[]>>;
}

const DocumentaryForm = (props: IDocumentaryFormProps) => {
  const { type, toggle, toggleSuccess, selectedRecord, setSelectedRecord, setSelectedRowKeys } = props;

  const fileInputRef = useRef<HTMLInputElement>(null);

  const [file, setFile] = useState<IFIle | null>(null);
  const [files, setFiles] = useState<IBodyFile[] | null>(null);

  const onOk = () => {
    toggle && toggle();
    toggleSuccess && toggleSuccess();
  };

  const { data, isLoading } = useGetEmployeeProfilesQuery();
  const { data: detail } = useGetDocumentaryByIdQuery(selectedRecord);
  const { mutate: create } = usePostDocumentaryMutation(onOk);
  const { mutate: update } = usePatchDocumentaryMutation(selectedRecord, onOk);
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFile(setFile);

  const { control, setValue, handleSubmit, formState } = useForm<DocumentarySchema>({
    resolver: zodResolver(documentarySchema),
  });

  const onSubmit: SubmitHandler<DocumentarySchema> = async values => {
    if (type === 'update') {
      update({
        documentNumber: values.documentNumber,
        dateStart: values.dateStart.toDate().toISOString(),
        group: values.group,
        type: values.type,
        content: values.content,
        signer: values.signer,
        recipient: values.recipient,
        archiveLocation: values.archiveLocation,
        senderOrReceiver: values.senderOrReceiver,
        attachmentsContentFile: file?.id,
        embedFiles: files,
      });
      setSelectedRecord(null);
      setSelectedRowKeys([]);
      setFiles(null);

      return;
    }

    create({
      documentNumber: values.documentNumber,
      dateStart: values.dateStart.toDate().toISOString(),
      group: values.group,
      type: values.type,
      content: values.content,
      signer: values.signer,
      recipient: values.recipient,
      archiveLocation: values.archiveLocation,
      senderOrReceiver: values.senderOrReceiver,
      attachmentsContentFile: file?.id,
      embedFiles: files,
    });
    setFiles(null);
  };

  useEffect(() => {
    if (file) setFiles(prev => [...(prev || []), { id: file?.id, fileName: file?.name }]);
  }, [file]);

  useEffect(() => {
    if (detail) {
      setValue('documentNumber', detail.documentNumber);
      setValue('dateStart', new DateObject(detail.dateStart).add(7, 'hours'));
      setValue('group', detail.group);
      setValue('type', detail.type);
      setValue('content', detail.content);
      setValue('signer', detail.signer);
      setValue('recipient', detail.recipient);
      setValue('archiveLocation', detail.archiveLocation);
      setValue('senderOrReceiver', detail.senderOrReceiver);

      if (detail?.attachments) {
        setFiles(detail?.attachments);
      }
    }
  }, [detail]);

  return (
    <Form id={FORM.ORDER} onSubmit={handleSubmit(onSubmit)}>
      <Card header='Thông tin chung' className='card-body-padding' classNameHeader='card-header-bold'>
        <Row>
          <Col md={6}>
            <FormInput control={control} id="documentaryNum" name="documentNumber" label="Số công văn" />
          </Col>

          <Col md={6}>
            <FormDatePicker setValue={setValue} control={control} id="date" name="dateStart" label="Ngày" formState={formState} />
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Nhóm" name="group" id="group" type="select">
              <option selected disabled>
                Chọn nhóm
              </option>
              <option value={DOCUMENTARY_GROUP.INTERNAL}>{documentaryGroupMapping(DOCUMENTARY_GROUP.INTERNAL)}</option>
              <option value={DOCUMENTARY_GROUP.OUTGOING}>{documentaryGroupMapping(DOCUMENTARY_GROUP.OUTGOING)}</option>
              <option value={DOCUMENTARY_GROUP.INCOMING}>{documentaryGroupMapping(DOCUMENTARY_GROUP.INCOMING)}</option>
            </FormInput>
          </Col>

          <Col md={6}>
            <FormInput control={control} label="Loại" name="type" id="type" type="select">
              <option selected disabled>
                Chọn loại
              </option>
              <option value={DOCUMENTARY_TYPE.ANNOUNCEMENT}>{documentaryTypeMapping(DOCUMENTARY_TYPE.ANNOUNCEMENT)}</option>
              <option value={DOCUMENTARY_TYPE.DOCUMENTARY}>{documentaryTypeMapping(DOCUMENTARY_TYPE.DOCUMENTARY)}</option>
              <option value={DOCUMENTARY_TYPE.RESPONSE}>{documentaryTypeMapping(DOCUMENTARY_TYPE.RESPONSE)}</option>
            </FormInput>
          </Col>

          <Col md={6}>
            <FormInput control={control} id="content" name="content" label="Nội dung" />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="signer"
              name="signer"
              placeholder="Chọn người ký"
              label="Người ký"
              options={data?.data?.map(e => ({
                label: e?.fullName,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="recipient" name="recipient" label="Nơi nhận" />
          </Col>

          <Col md={6}>
            <FormInput control={control} id="archiveLocation" name="archiveLocation" label="Nơi lưu" />
          </Col>

          <Col md={6}>
            <FormSelect
              control={control}
              id="senderOrReceiver"
              name="senderOrReceiver"
              placeholder="Chọn người gửi / nhận"
              label="Người gửi / nhận"
              options={data?.data?.map(e => ({
                label: e?.fullName,
                value: e?.id,
              }))}
              isLoading={isLoading}
            />
          </Col>
        </Row>
      </Card>

      <div className="divider" />

      <Card header='Tải tệp đính kèm' className='card-body-padding' classNameHeader='card-header-bold'>
        <Button
          key={new Date().getMilliseconds()}
          type="button"
          color="primary"
          onClick={() => fileInputRef.current?.click()}
          className="btn-upload"
          loading={loadingUpload}
        >
          <Flex align="center" gap={8}>
            <img src="content/images/vuesax/linear/paperclip.svg" alt="attach" />
            Đính kèm
            <InputFile onFileChange={file => uploadFile(file)} name="fileAttachment" hidden ref={fileInputRef} />
          </Flex>
        </Button>

        {!!files?.length && (
          <>
            <div className="divider" />
            <Flex flexWrap="wrap" gap={12}>
              {files?.map((item: IBodyFile) => {
                return (
                  <AttachmentPreview
                    name={item?.fileName}
                    onClose={() => setFiles(prev => prev.filter(e => e?.id !== item?.id))}
                    fileUrl={`${FILE_UTIL}/${item?.id}`}
                  />
                );
              })}
            </Flex>
          </>
        )}
      </Card>
    </Form>
  );
};

export default DocumentaryForm;
