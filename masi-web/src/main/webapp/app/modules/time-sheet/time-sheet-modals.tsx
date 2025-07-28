import './time-sheet.scss';
import React from 'react';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import DatePicker from 'app/components/date-picker/date-picker';
import { FORM } from 'app/shared/model/enumerations/form.model';
import UpdateTimeKeepForm from './update-time-keep-form';
import { TimeCheckType } from './time-sheet';
import { MUTATION_KEY } from 'app/constants/query-key';
import { useIsMutating } from '@tanstack/react-query';

const { UPDATE_TIME_KEEPING } = MUTATION_KEY;

// MODAL TIME CHECK
interface IModalTimeCheck {
  isOpen: boolean;
  toggle: () => void;
  inOrOut: TimeCheckType;
}

export const ModalTimeCheck = (props: IModalTimeCheck) => {
  const { isOpen, toggle, inOrOut } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={false}
      className="modal-time-check-success"
      titleHeader='Chấm công thành công'
    >
      <Typography level={6}>Bạn đã chấm công {inOrOut === TimeCheckType.CHECK_IN ? 'vào' : 'ra'} hôm nay thành công</Typography>
    </Modal>
  );
};

// MODAL UPDATE TIME CHECK
interface IModalUpdateTimeCheck {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  selectedRecord: string | null;
}

export const ModalUpdateTimeCheck = (props: IModalUpdateTimeCheck) => {
  const { isOpen, toggle, toggleSuccess, selectedRecord } = props;

  const isMutatingTimeKeeping = useIsMutating({
    mutationKey: [UPDATE_TIME_KEEPING],
  });

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-timesheet-bulk"
      okText="Cập nhật"
      okSubmitForm={FORM.UPDATE_TIMESHEET}
      disabledOk={!!isMutatingTimeKeeping}
      titleHeader='Cập nhật chấm công'
      level={5}
    >
      <UpdateTimeKeepForm toggle={toggle} toggleSuccess={toggleSuccess} selectedRecord={selectedRecord} />
    </Modal>
  );
};

// MODAL UPDATE TIME CHECK SUCCESS
interface IModalUpdateTimeCheckSuccess {
  isOpen: boolean;
  toggle: () => void;
  cancel: boolean;
}

export const ModalUpdateTimeCheckSuccess = (props: IModalUpdateTimeCheckSuccess) => {
  const { isOpen, toggle, cancel } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      cancel={cancel}
      className="modal-timesheet-bulk-success"
      titleHeader='Cập nhật thành công'
    >
      <Typography level={5}>Bạn đã cập nhật BCC thành công</Typography>
    </Modal>
  );
};

// MODAL EXPORT TIME SHEET
interface IModalExportTimeSheet {
  isOpen: boolean;
  toggle: () => void;
}

export const ModalExportTimeSheet = (props: IModalExportTimeSheet) => {
  const { isOpen, toggle } = props;

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      className="modal-timesheet-export"
      titleHeader='Xuất BCC'
    >
      <DatePicker id="from" name="from" range />
    </Modal>
  );
};
