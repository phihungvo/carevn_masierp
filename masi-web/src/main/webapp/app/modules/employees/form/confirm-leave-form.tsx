import { zodResolver } from '@hookform/resolvers/zod';
import AttachmentPreview from 'app/components/attachment-preview/attachment-preview';
import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import Form from 'app/components/form/form';
import FormDatePicker from 'app/components/form/form-date-picker';
import FormInput from 'app/components/form/form-input';
import InputFile from 'app/components/input/input-file';
import { FILE_UTIL } from 'app/constants/common';
import useAccount from 'app/hooks/use-account';
import useEmployee from 'app/hooks/use-employee';
import useFile from 'app/hooks/use-file';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { IFIle } from 'app/shared/model/file.model';
import { ConfirmLeaveFormSchema, confirmLeaveSchema } from 'app/validation/employee.validation';
import React, { useRef } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { Col, Row } from 'reactstrap';

const { usePatchConfirmLeaveMutation } = useEmployee;
const { usePostFiles } = useFile;
const { usePatchDisableEmployeeProfileMutation } = useEmployee;
const { useToggleActivate } = useAccount;

interface IConfirmLeaveFormProps {
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string;
  setSelectedRecord: (value: string) => void;
}

const ConfirmLeaveForm = (props: IConfirmLeaveFormProps) => {
  const { toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const fileInputRef = useRef<HTMLInputElement>(null);
  const [fileUploads, setFileUploads] = React.useState<IFIle[]>([]);

  const { control, handleSubmit, setValue, formState } = useForm<ConfirmLeaveFormSchema>({
    resolver: zodResolver(confirmLeaveSchema),
  });

  const { mutate } = usePatchConfirmLeaveMutation(toggle, toggleSuccess);
  const { mutate: uploadFile, isPending: loadingUpload } = usePostFiles(setFileUploads);
  const { mutate: mutateDisableEmployeeProfile } = usePatchDisableEmployeeProfileMutation(selectedRecord);
  const { mutate: toggleActivate } = useToggleActivate();

  const onSubmit: SubmitHandler<ConfirmLeaveFormSchema> = data => {
    mutate({
      id: selectedRecord,
      submissionDate: data.submissionDate?.toDate()?.toISOString(),
      reason: data.reason,
      recruitmentSolution: data.recruitmentSolution,
      hrSolution: data.hrSolution,
      leaveDate: data.leaveDate?.toDate()?.toISOString(),
      fileAttachmentIds: fileUploads?.map(file => file?.id),
    });
    mutateDisableEmployeeProfile();
    setSelectedRecord(null);
    toggleActivate(selectedRecord);
  };

  return (
    <Form id={FORM.CONFIRM_LEAVE} onSubmit={handleSubmit(onSubmit)}>
      <Row>
        <Col md={6}>
          <FormDatePicker setValue={setValue} control={control} name="submissionDate" label="Ngày nộp đơn nghỉ việc" formState={formState} />
        </Col>

        <Col md={6}>
          <FormInput control={control} name="reason" label="Lý do nghỉ việc" />
        </Col>
      </Row>

      <Row>
        <Col md={6}>
          <FormDatePicker setValue={setValue} control={control} name="leaveDate" label="Ngày nghỉ việc" formState={formState} />
        </Col>

        <Col md={6}>
          <FormInput control={control} name="hrSolution" label="Giải quyết của BP HCNS" type="textarea" />
        </Col>
      </Row>
      {/* 
      <Row>
        <Col md={6}>
          <FormInput control={control} name="recruitmentSolution" label="Giải quyết của BP tuyển dụng" type="textarea" />
        </Col>
      </Row> */}

      <Button
        key={new Date().getMilliseconds()}
        type="button"
        color="primary"
        onClick={() => fileInputRef.current?.click()}
        className="btn-upload"
        loading={loadingUpload}
        disabled={loadingUpload}
      >
        <Flex align="center" gap={8}>
          <img src="content/images/vuesax/linear/paperclip.svg" alt="attach" />
          Đính kèm
          <InputFile onFileChange={file => uploadFile(file)} name="fileAttachment" hidden ref={fileInputRef} />
        </Flex>
      </Button>

      <div className="divider" />

      {!!fileUploads?.length && (
        <>
          <p className="attachment">Tệp đính kèm</p>
          <Flex gap={8}>
            {fileUploads?.map((file, index) => (
              <React.Fragment key={file?.id}>
                <AttachmentPreview
                  name={file.name}
                  onClose={() => setFileUploads(fileUploads.filter(e => e.id !== file.id))}
                  fileUrl={`${FILE_UTIL}/${file.id}`}
                />
                {/* {fileSizeNotValid && <p className="text-danger">Kích thước tệp không được vượt quá 5MB</p>} */}
              </React.Fragment>
            ))}
          </Flex>
        </>
      )}
    </Form>
  );
};

export default ConfirmLeaveForm;
