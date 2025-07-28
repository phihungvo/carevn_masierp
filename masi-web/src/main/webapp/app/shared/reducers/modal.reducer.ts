import { createSlice, PayloadAction } from '@reduxjs/toolkit'

export type ModalReducerState = {
  isOpen: boolean,
  title: string,
  content: string | React.ReactNode,
  cancelText: string,
  okText: string,
  onOK: () => void,
  onCancel: () => void,
  footer?: React.ReactNode
}

type OptionsModal = {
  options?: {
    initState?: boolean
  }
}

export const initialModalState: ModalReducerState = {
  isOpen: false,
  title: '',
  content: '',
  cancelText: '',
  okText: '',
  onOK: () => {},
  onCancel: () => {},
  footer: null
}

export const modalSlice = createSlice({
  name: 'modal',
  initialState: initialModalState,
  reducers: {
    toggleModal: (state, action: PayloadAction<Partial<ModalReducerState> & OptionsModal>) => {
      if (action?.payload?.options?.initState) return { ...initialModalState, ...action.payload }
      return { ...state, ...action.payload }
    }
  },
})

export const { toggleModal } = modalSlice.actions

export default modalSlice.reducer
