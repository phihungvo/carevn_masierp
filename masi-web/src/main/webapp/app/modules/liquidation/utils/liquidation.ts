import { Color, ColorType } from "app/shared/model/enumerations/color.model"
import { LIQUIDATION_STATUS } from "app/shared/model/enumerations/liquidation"

const liquidationStatus = {
    [LIQUIDATION_STATUS.UNPROCESSED]: 'Chưa xử lý',
    [LIQUIDATION_STATUS.WAITING_FOR_PROCESSING]: 'Đợi xử lý',
    [LIQUIDATION_STATUS.PROCESSED]: 'Đã xử lý',
    [LIQUIDATION_STATUS.CANCEL]: 'Hủy',
}

const liquidationStatusColor = {
    [LIQUIDATION_STATUS.UNPROCESSED]: Color.PRIMARY,
    [LIQUIDATION_STATUS.WAITING_FOR_PROCESSING]: Color.WARNING,
    [LIQUIDATION_STATUS.PROCESSED]: Color.SUCCESS,
    [LIQUIDATION_STATUS.CANCEL]: undefined,
}

export const liquidationMappingStatusText = (text: LIQUIDATION_STATUS): string => {
    return liquidationStatus[text] || ''
}

export const liquidationMappingStatusColor = (text: LIQUIDATION_STATUS): ColorType => {
    return liquidationStatusColor[text] || ''
}
