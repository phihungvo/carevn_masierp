import { createSlice } from '@reduxjs/toolkit';

export const initialState = {
  workItemId: undefined,
};

export type ProductionProcessState = Readonly<typeof initialState>;

export const ProductionProcessSlice = createSlice({
  name: 'productionProcess',
  initialState,
  reducers: {
    setWorkItemId: (state, action) => {
      state.workItemId = action.payload;
    },
  },
});

export const { setWorkItemId } = ProductionProcessSlice.actions;

export default ProductionProcessSlice.reducer;
