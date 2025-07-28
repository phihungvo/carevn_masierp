import { PRODUCTION_PROCESS_STATUS } from 'app/shared/model/enumerations/production-process.model';

export const handleDisableButtonStart = (data: any, record: any): boolean => {
  // Vòng lặp sẽ lặp qua từng các công đoạn.
  for (let i = 0; i < data?.length; i++) {
    // Phần này handle công đoạn đầu tiên.
    // Nếu như là công đoạn đầu tiên của action hiện tại và với công đoạn đầu tiên.
    if (data[i]?.checklistOrder === 1 && record?.checklistOrder === 1) {
      // Nếu trạng thái của công đoạn đó là thành công | hoặc đang sản xuất thì nó sẽ trả về true và disable đi cái nút start nếu không đúng thì trả vể false.
      if (data[i]?.status === PRODUCTION_PROCESS_STATUS.COMPLETED || record?.status === PRODUCTION_PROCESS_STATUS.RUNNING) return true;
      return false;
    }
    // Phần này handle công đoạn 2 -> 5 | 6.
    // Nếu trạng thái của công đoạn đó là thành công | hoặc đang sản xuất thì nó sẽ trả về true và disable đi cái nút start nếu không đúng thì trả vể false.
    if (record?.status === PRODUCTION_PROCESS_STATUS.COMPLETED || record?.status === PRODUCTION_PROCESS_STATUS.RUNNING) return true;

    // Phần này check xem công đoạn trước là trạng thái gì? và nếu như hoàn thành thì nó sẽ trả về false để có thể start công đoạn hiện tại, sai thì nó sẽ trả về true.
    // "data[record.checklistOrder - 2]?.status" đây để lấy công đoạn trước, trừ 2 ở đây vì data là 1 cái mảng và nếu như handle xuống được đến đây sẽ là phần tử thứ 2,
    // thế nên nó sẽ có checklistOrder là 2 và mình cần lấy phần từ đầu tiên để check thì trừ đi 2.
    if (data[record.checklistOrder - 2]?.status === PRODUCTION_PROCESS_STATUS.COMPLETED) return false;

    return true;
  }
  return false;
};

export const checkDisabledStartBtn = (parent: any, rowNumber: number) => {
  const isOrderProcess = parent?.length ===2;
  if(isOrderProcess){
    return false; // if the process has only 2 steps, that is order process, so always enable start button
  }
  // If current row is first production process -> Disabled start button if the status is COMPLETED or RUNNING

  // If current row is after first production process -> Disabled start button if the previous production process is COMPLETED or the current production process is COMPLETED or RUNNING
  return rowNumber !== 0
    ? parent?.[rowNumber - 1]?.status !== PRODUCTION_PROCESS_STATUS.COMPLETED ||
        parent?.[rowNumber]?.status === PRODUCTION_PROCESS_STATUS.RUNNING ||
        parent?.[rowNumber]?.status === PRODUCTION_PROCESS_STATUS.COMPLETED
    : parent?.[rowNumber]?.status === PRODUCTION_PROCESS_STATUS.COMPLETED ||
        parent?.[rowNumber]?.status === PRODUCTION_PROCESS_STATUS.RUNNING;
};
