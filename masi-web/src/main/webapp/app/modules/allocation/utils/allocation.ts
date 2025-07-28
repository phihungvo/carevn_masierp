import { Color, ColorType } from "app/shared/model/enumerations/color.model"
import { ALLOCATION_TYPE, ALLOCATION_STATUS } from "app/shared/model/enumerations/allocation"

const allocationType = {
    [ALLOCATION_TYPE.INTERNAL]: 'Nội bộ',
    [ALLOCATION_TYPE.OUTSIDE]: 'Ngoại bộ',
}

const allocationStatus = {
    [ALLOCATION_STATUS.UNPROCESSED]: 'Chưa xử lý',
    [ALLOCATION_STATUS.WAITING_FOR_PROCESSING]: 'Đợi xử lý',
    [ALLOCATION_STATUS.PROCESSED]: 'Đã xử lý',
    [ALLOCATION_STATUS.CANCEL]: 'Hủy',
}

const allocationStatusColor = {
    [ALLOCATION_STATUS.UNPROCESSED]: Color.PRIMARY,
    [ALLOCATION_STATUS.WAITING_FOR_PROCESSING]: Color.WARNING,
    [ALLOCATION_STATUS.PROCESSED]: Color.SUCCESS,
    [ALLOCATION_STATUS.CANCEL]: undefined,
}

export const allocationMappingType = (text: ALLOCATION_TYPE): string => {
    return allocationType[text] || ''
}

export const allocationMappingStatusText = (text: ALLOCATION_STATUS): string => {
    return allocationStatus[text] || ''
}

export const allocationMappingStatusColor = (text: ALLOCATION_STATUS): ColorType => {
    return allocationStatusColor[text] || ''
}
