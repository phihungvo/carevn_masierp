import Modal from 'app/components/modal/modal';
import { FORM } from 'app/shared/model/enumerations/form.model';
import { ISampleDisposal } from 'app/shared/model/production-quality-control.model';
import CancelTestingTemplateForm from './cancel-testing-template-form';
import { PRODUCTION_QUALITY_STATUS } from 'app/shared/model/enumerations/production-quality-control.model';
import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';

interface CancelTestingTemplateProps {
  isOpen: boolean;
  toggle: () => void;
  toggleSuccess: () => void;
  detail?: ISampleDisposal;
  selectRecord?: string;
  status?: PRODUCTION_QUALITY_STATUS;
  parentId?: string;
  statusM?: MANUFACTURE_ORDER_STATUS;
}
const CancelTestingTemplate = (props: CancelTestingTemplateProps) => {
  const {
    isOpen,
    toggle,
    toggleSuccess,
    detail,
    selectRecord,
    status,
    parentId,
    statusM,
  } = props;

  const toggleCancelRequestSuccess = () => {
    toggleSuccess();
  };

  return (
    <Modal
      isOpen={isOpen}
      toggle={toggle}
      okSubmitForm={FORM.CANCEL_TESTING_TEMPLATE}
      titleHeader="Đơn hủy mẫu kiểm định"
      style={{ width: '600px' }}
    >
      <CancelTestingTemplateForm
        toggle={toggleCancelRequestSuccess}
        detail={detail}
        selectRecord={selectRecord}
        status={status}
        statusM={statusM}
        parentId={parentId}
      />
    </Modal>
  );
};

export default CancelTestingTemplate;
