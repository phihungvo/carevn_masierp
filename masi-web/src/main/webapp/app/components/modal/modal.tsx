import './modal.scss';
import React from 'react';
import {
  ModalBody,
  ModalFooter,
  ModalHeader,
  ModalProps,
  Modal as ModalStrap,
} from 'reactstrap';
import Button from '../button/button';
import { FormType } from 'app/shared/model/enumerations/form.model';
import { Typography } from '../typography/typography';

interface IModal extends ModalProps {
  isOpen: boolean;
  toggle: () => void;
  header?: React.ReactNode;
  children: React.ReactNode;
  footer?: React.ReactNode;
  onOk?: (params: any) => void;
  cancel?: boolean;
  ok?: boolean;
  onCancel?: () => void;
  okText?: string;
  cancelText?: string;
  okSubmitForm?: FormType; // Submit form type must be the same as form id to trigger submit form
  disabledOk?: boolean;
  loadingOk?: boolean;
  titleHeader?: React.ReactNode;
  level?: 1 | 2 | 3 | 4 | 5 | 6 | 'paragraph' | 'text';
  style?: React.CSSProperties;
}

const Modal = (props: IModal) => {
  const {
    isOpen,
    toggle,
    header,
    children,
    footer,
    onOk,
    cancel = true,
    ok = true,
    onCancel,
    cancelText,
    okText,
    okSubmitForm,
    backdrop = false,
    disabledOk,
    loadingOk,
    titleHeader = 'Bộ lọc',
    level = 5,
    style,
    ...rest
  } = props;

  return (
    <>
      <ModalStrap
        {...rest}
        isOpen={isOpen}
        toggle={toggle}
        backdrop={backdrop}
        style={{ minWidth: '600px', maxWidth: '80%', ...style }}
        zIndex={98}
      >
        <ModalHeader>
          <Typography level={3}>{titleHeader}</Typography>
          <img
            src="content/images/vuesax/linear/modal-close.svg"
            alt="close"
            className="modal-close"
            onClick={toggle}
          />
        </ModalHeader>
        <ModalBody>{children}</ModalBody>
        <div className="divider-modal-footer"></div>
        <ModalFooter>
          {footer ? (
            footer
          ) : (
            <>
              {cancel && (
                <Button
                  className="button-cancel"
                  outline
                  onClick={onCancel ? onCancel : toggle}
                >
                  {cancelText ? cancelText : 'Hủy'}
                </Button>
              )}
              {ok && (
                <Button
                  className="button-primary"
                  disabled={disabledOk}
                  loading={loadingOk !== undefined ? loadingOk : disabledOk}
                  color="primary"
                  {...(!okSubmitForm && { onClick: onOk ? onOk : toggle })}
                  form={okSubmitForm}
                >
                  {okText ? okText : 'Đồng ý'}
                </Button>
              )}
            </>
          )}
        </ModalFooter>
      </ModalStrap>
      {isOpen && <div className="modal-backdrop fade show z-0"></div>}
    </>
  );
};

export default Modal;
