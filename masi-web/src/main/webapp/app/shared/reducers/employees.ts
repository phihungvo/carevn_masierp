import { createSlice } from '@reduxjs/toolkit';

export const initialState = {
  importResponse: undefined,
};

export type EmployeeState = Readonly<typeof initialState>;

export const EmployeeSlice = createSlice({
  name: 'employees',
  initialState,
  reducers: {
    setImportResponse: (state, action) => {
      state.importResponse = action.payload;
    },
  },
});

export const { setImportResponse } = EmployeeSlice.actions;

export default EmployeeSlice.reducer;
