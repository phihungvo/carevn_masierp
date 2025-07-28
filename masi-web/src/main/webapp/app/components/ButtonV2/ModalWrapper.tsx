import { ComponentProps, useState } from "react"
import Modal from "../modal/modal"

type RenderModal = {
    onToggle: () => void
    onOk: () => void
    onCancel: () => void
    isOpen: boolean
}

type Props = Partial<ComponentProps<typeof Modal>> & {
    renderTarget: ({ onToggle }) => React.ReactNode
    renderModal: (args: RenderModal) => React.ReactNode
}

const ModalWrapper = (props: Props) => {
    const { renderTarget, toggle, onOk, onCancel, renderModal, ...modalProps } = props

    const [openModal, setOpenModal] = useState<boolean>(false)

    const onToggle = () => {
        setOpenModal(!openModal)
        toggle?.()
    }

    const onOkModal = () => {
        onToggle()
        onOk?.({})
    }

    const onCancelModal = () => {
        onToggle()
        onCancel?.()
    }

  return (
    <>
        {renderTarget({ onToggle })}
        <Modal
            {...modalProps}
            isOpen={openModal}
            toggle={onToggle}
            onOk={onOkModal}
            onCancel={onCancelModal}
        >
            {renderModal({ onToggle, onCancel: onCancelModal, onOk: onOkModal, isOpen: openModal })}
        </Modal>
    </>
  )
}

export default ModalWrapper