import { IRootState, useAppDispatch, useAppSelector } from 'app/config/store';
import { ModalReducerState, toggleModal } from 'app/shared/reducers/modal.reducer';

const useModalRedux = () => {
  const modalState = useAppSelector<ModalReducerState>((state: IRootState) => state?.modal);
  const dispatch = useAppDispatch();

  const closeModal = () => dispatch(toggleModal({ isOpen: false }))

  const openModal = () => dispatch(toggleModal({ isOpen: true }))

  const changeContentModal = (payload: Omit<ModalReducerState, 'isOpen'>) => {
    dispatch(toggleModal(payload))
  }

  const handleToggleModal = (payload: Partial<ModalReducerState>) => {
    dispatch(toggleModal(payload))
  }

  const handleToggleSuccessModal = (payload: Pick<ModalReducerState, 'content'>, isBack = true) => {
    dispatch(toggleModal({
      isOpen: true,
      title: 'Thông báo',
      content: payload.content,
      okText: 'Thoát',
      onOK: () => {
        closeModal()
        isBack && window.history.go(-1)
      },
      options: { initState: true }
    }))
  }

  const handleToggleFailModal = (payload: Pick<ModalReducerState, 'content'>, isBack = true) => {
    dispatch(toggleModal({
      isOpen: true,
      title: 'Thông báo',
      content: payload.content,
      okText: 'Tiếp tục',
      cancelText: 'Thoát',
      onCancel: () => {
        closeModal()
        isBack && window.history.go(-1)
      },
      onOK: closeModal,
      options: { initState: true }
    }))
  }

  const handleToggleFailModalWithoutOkText = (payload: Pick<ModalReducerState, 'content'>) => {
    dispatch(toggleModal({
      isOpen: true,
      title: 'Thông báo',
      content: payload.content,
      cancelText: 'Thoát',
      onCancel: closeModal,
      options: { initState: true }
    }))
  }

  return {
    modalState,
    closeModal,
    openModal,
    changeContentModal,
    handleToggleModal,
    handleToggleSuccessModal,
    handleToggleFailModal,
    handleToggleFailModalWithoutOkText
  }
}

export default useModalRedux
