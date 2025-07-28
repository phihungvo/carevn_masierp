import Flex from 'app/components/flex/flex';
import InputFile from 'app/components/input/input-file';
import React, { useRef } from 'react';
import { IProfileFile } from '../modals/employee-upload-modal';
import useFile from 'app/hooks/use-file';
import { PROFILE_ATTACHMENT_TYPE } from 'app/shared/model/enumerations/employee.model';

const { usePostFile } = useFile;

interface IUploadFileProps {
  label: string;
  setFile?: React.Dispatch<React.SetStateAction<IProfileFile>>;
  fileKey?: `${PROFILE_ATTACHMENT_TYPE}`;
}

const UploadFile = (props: IUploadFileProps) => {
  const { label, setFile, fileKey } = props;

  const { mutate: uploadFile } = usePostFile();

  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleUploadFile = (file: File) => {
    uploadFile(file, {
      onSuccess: data => {
        setFile(prev => ({ ...prev, [fileKey]: [...prev?.[fileKey], data?.data] }))
      },
    });
  };

  return (
    <div className="employee-upload-container" onClick={() => fileInputRef.current?.click()}>
      <Flex justify="space-between">
        <div className="employee-upload-label">
          <span className="employee-upload-label-required">*</span>
          {label}
        </div>
        <img src="content/images/vuesax/linear/upload.svg" />
      </Flex>
      <span className="employee-upload-sub">DOC, DOCX, PDF (2MB)</span>

      <InputFile
        {...(setFile && {
          onFileChange: file => handleUploadFile(file),
        })}
        name="fileAttachment"
        hidden
        ref={fileInputRef}
      />
    </div>
  );
};

export default UploadFile;
