import { createSlice } from '@reduxjs/toolkit';

export const initialState = {
  isUpdate: false,
};

export type QuotationState = Readonly<typeof initialState>;

export const QuotationSlice = createSlice({
  name: 'quotation',
  initialState,
  reducers: {
    setIsUpdate: (state, action) => {
      state.isUpdate = action.payload;
    },
  },
});

export const { setIsUpdate } = QuotationSlice.actions;

export default QuotationSlice.reducer;
