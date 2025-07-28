import { FormGroup, Label } from 'reactstrap';
import React, { useEffect, useState } from 'react';

import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import Input from 'app/components/input/input';
import useEmployee from 'app/hooks/use-employee';
import UploadFile from '../components/upload-file';
import { IFIle } from 'app/shared/model/file.model';
import Card from 'app/components/card/card';
import ListImage from '../components/list-image/list-image';
import { IProfileAttachment } from 'app/shared/model/employee.model';
import { PROFILE_ATTACHMENT_TYPE } from 'app/shared/model/enumerations/employee.model';
import { formatFilesSubmit } from '../util/format-files-submit';

const { usePatchProfileAttachments, useGetEmployeeProfileByIdQuery } = useEmployee;

export interface IProfileFile {
  CMND?: IFIle[] | null;
  HK?: IFIle[] | null;
  SYLL?: IFIle[] | null;
  DON_XV?: IFIle[] | null;
  GKSK?: IFIle[] | null;
  GCK?: IFIle[] | null;
  OTHER: IFIle[] | null;
}

export enum EProfileFile {
  CMND = 'CMND',
  HK = 'HK',
  SYLL = 'SYLL',
  DON_XV = 'DON_XV',
  GKSK = 'GKSK',
  GCK = 'GCK',
  OTHER = 'OTHER',
}

interface IEmployeeUploadModalProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
  setSelectedRecord: (record: string | null) => void;
}

const EmployeeUploadModal = (props: IEmployeeUploadModalProps) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord, setSelectedRecord } = props;

  const [fileProfile, setFileProfile] = useState<IProfileFile>({
    CMND: [],
    HK: [],
    SYLL: [],
    DON_XV: [],
    GKSK: [],
    GCK: [],
    OTHER: [],
  });

  const { data } = useGetEmployeeProfileByIdQuery(selectedRecord);

  useEffect(() => {
    if (data) {
      const profileFiles = {
        CMND: [],
        HK: [],
        SYLL: [],
        DON_XV: [],
        GKSK: [],
        GCK: [],
        OTHER: [],
      };

      data?.files?.forEach(item => {
        profileFiles[item.type] = [...profileFiles[item.type], {
          id: item?.fileAttachment?.id,
          name: item?.fileAttachment?.name,
          path: item?.fileAttachment?.name,
        }]
      })

      setFileProfile(profileFiles)
    }
  }, [data])

  const { mutate, isPending } = usePatchProfileAttachments(toggle, toggleSuccess);

  const onOk = (): void => {
    let body: IProfileAttachment[] = [];

    Object.keys(fileProfile).forEach((key: string): void => {
      fileProfile[key].forEach((file: IFIle) => {
        formatFilesSubmit(body, file, key as PROFILE_ATTACHMENT_TYPE, selectedRecord);
      });
    });

    mutate(body);
  };

  useEffect(() => {
    if (!isOpen) {
      setFileProfile({
        CMND: [],
        HK: [],
        SYLL: [],
        DON_XV: [],
        GKSK: [],
        GCK: [],
        OTHER: [],
      });
      setSelectedRecord(null);
    }
  }, [isOpen]);

  const handleChange = (key: EProfileFile) => (e: React.ChangeEvent<HTMLInputElement>) => {
    if (fileProfile[key]?.length === 0) setFileProfile(prev => ({ ...prev, [key]: [{ id: null, name: '', path: null }] }));
    else setFileProfile(prev => ({ ...prev, [key]: [] }));
  }

  const isCheck =
    !!!fileProfile[EProfileFile.CMND].length &&
    !!!fileProfile[EProfileFile.DON_XV].length &&
    !!!fileProfile[EProfileFile.GCK].length &&
    !!!fileProfile[EProfileFile.GKSK].length &&
    !!!fileProfile[EProfileFile.HK].length &&
    !!!fileProfile[EProfileFile.SYLL].length;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      footer={null}
      className="modal-default"
      fullscreen
      onOk={onOk}
      loadingOk={isPending}
      titleHeader='Tải lên hồ sơ đính kèm'
    >
      <Card header='Đính kèm' className='card-body-padding ' classNameHeader='card-header-bold'>
        <div className='container'>
          <Flex direction="column">
            <Label for="indentity">CMND/CCCD</Label>
            <UploadFile label="Tải lên CMND/CCCD" setFile={setFileProfile} fileKey="CMND" />
            <ListImage name={EProfileFile.CMND} fileProfile={fileProfile} setFileProfile={setFileProfile} />
            <FormGroup check>
              <Label check>Đã tải lên</Label>
              <Input type="checkbox" readOnly name="identityCheck" disabled={isCheck} checked={!!fileProfile[EProfileFile.CMND]?.length} onChange={handleChange(EProfileFile.CMND)} />
            </FormGroup>
          </Flex>

          <Flex direction="column">
            <Label for="hk">Hộ Khẩu</Label>
            <UploadFile label="Tải lên HK" setFile={setFileProfile} fileKey="HK" />
            <ListImage name={EProfileFile.HK} fileProfile={fileProfile} setFileProfile={setFileProfile} />
            <FormGroup check>
              <Label check>Đã tải lên</Label>
              <Input type="checkbox" readOnly name="hkCheck" disabled={isCheck} checked={!!fileProfile[EProfileFile.HK]?.length} onChange={handleChange(EProfileFile.HK)} />
            </FormGroup>
          </Flex>

          <Flex direction="column">
            <Label for="syll">Sơ Yếu Lý Lịch</Label>
            <UploadFile label="Tải lên SYLL" setFile={setFileProfile} fileKey="SYLL" />
            <ListImage name={EProfileFile.SYLL} fileProfile={fileProfile} setFileProfile={setFileProfile} />
            <FormGroup check>
              <Label check>Đã tải lên</Label>
              <Input type="checkbox" readOnly name="syllCheck" disabled={isCheck} checked={!!fileProfile[EProfileFile.SYLL]?.length} onChange={handleChange(EProfileFile.SYLL)} />
            </FormGroup>
          </Flex>

          <Flex direction="column">
            <Label for="application">Đơn Xin Việc</Label>
            <UploadFile label="Tải lên đơn XV" setFile={setFileProfile} fileKey="DON_XV" />
            <ListImage name={EProfileFile.DON_XV} fileProfile={fileProfile} setFileProfile={setFileProfile} />
            <FormGroup check>
              <Label check>Đã tải lên</Label>
              <Input type="checkbox" readOnly name="applicationCheck" disabled={isCheck} checked={!!fileProfile[EProfileFile.DON_XV]?.length} onChange={handleChange(EProfileFile.DON_XV)} />
            </FormGroup>
          </Flex>

          <Flex direction="column">
            <Label for="gksk">Giấy Khám Sức Khỏe</Label>
            <UploadFile label="Tải lên GKSK" setFile={setFileProfile} fileKey="GKSK" />
            <ListImage name={EProfileFile.GKSK} fileProfile={fileProfile} setFileProfile={setFileProfile} />
            <FormGroup check>
              <Label check>Đã tải lên</Label>
              <Input type="checkbox" readOnly name="gkskCheck" disabled={isCheck} checked={!!fileProfile[EProfileFile.GKSK]?.length} onChange={handleChange(EProfileFile.GKSK)} />
            </FormGroup>
          </Flex>

          <Flex direction="column">
            <Label for="gck">Giấy Cam Kết</Label>
            <UploadFile label="Tải lên GCK" setFile={setFileProfile} fileKey="GCK" />
            <ListImage name={EProfileFile.GCK} fileProfile={fileProfile} setFileProfile={setFileProfile} />
            <FormGroup check>
              <Label check>Đã tải lên</Label>
              <Input type="checkbox" readOnly name="gckCheck" disabled={isCheck} checked={!!fileProfile[EProfileFile.GCK]?.length} onChange={handleChange(EProfileFile.GCK)} />
            </FormGroup>
          </Flex>

          <Flex direction="column">
            <Label for="other">Khác</Label>
            <UploadFile label="Tải lên tệp khác" setFile={setFileProfile} fileKey="OTHER" />
            <ListImage name={EProfileFile.OTHER} fileProfile={fileProfile} setFileProfile={setFileProfile} />
            <FormGroup check>
              <Label check>Đã tải lên {fileProfile?.OTHER?.length ? `${fileProfile?.OTHER.length} tệp` : ''}</Label>
              <Input type="checkbox" readOnly name="other" disabled={isCheck} checked={!!fileProfile[EProfileFile.OTHER]?.length} onChange={handleChange(EProfileFile.OTHER)} />
            </FormGroup>
          </Flex>
        </div>
      </Card>
    </Modal>
  );
};

export default EmployeeUploadModal;
