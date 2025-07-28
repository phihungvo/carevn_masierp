import { ECAll_CENTER_TYPE } from "app/shared/model/enumerations/call-center"
import { Color, ColorType } from "app/shared/model/enumerations/color.model"
import { DEPRECIATION_STATUS } from "app/shared/model/enumerations/depreciation"

const depreciationType = {
    [ECAll_CENTER_TYPE.INTERNAL]: 'Nội bộ',
    [ECAll_CENTER_TYPE.OUTSIDE]: 'Ngoại bộ',
}

const depreciationStatus = {
    [DEPRECIATION_STATUS.NEW]: 'Mới',
    [DEPRECIATION_STATUS.APPROVED]: 'Đã duyệt',
    [DEPRECIATION_STATUS.PENDING]: 'Chờ duyệt',
    [DEPRECIATION_STATUS.REJECT]: 'Từ chối',
}

const depreciationStatusColor = {
    [DEPRECIATION_STATUS.NEW]: Color.PRIMARY,
    [DEPRECIATION_STATUS.PENDING]: Color.WARNING,
    [DEPRECIATION_STATUS.APPROVED]: Color.SUCCESS,
}

export const depreciationMappingType = (text: ECAll_CENTER_TYPE): string => {
    return depreciationType[text] || ''
}

export const depreciationMappingStatusText = (text: DEPRECIATION_STATUS): string => {
    return depreciationStatus[text] || ''
}

export const depreciationMappingStatusColor = (text: DEPRECIATION_STATUS): ColorType => {
    return depreciationStatusColor[text] || ''
}