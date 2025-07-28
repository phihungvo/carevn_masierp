
export enum StatusEnum {
    DEPRECIATION = 'DEPRECIATION',
    ACTIVE = 'ACTIVE',
    LIQUIDATION = 'LIQUIDATION',
    SAVE_STORAGE = 'SAVE_STORAGE',
    CANCEL = 'CANCEL',
    EXPIRED = 'EXPIRED',
}

export const statusLabel = {
    DEPRECIATION: 'Không KH/PB',
    ACTIVE: 'Hoạt động',
    LIQUIDATION: 'Thanh lý',
    SAVE_STORAGE: 'Lưu kho',
    CANCEL: 'Hủy',
    EXPIRED: 'Hết hạn'
}

export const statusSelectData = {
    arr: [
        {
            label: statusLabel.DEPRECIATION,
            value: StatusEnum.DEPRECIATION
        },
        {
            label: statusLabel.ACTIVE,
            value: StatusEnum.ACTIVE
        },
        {
            label: statusLabel.LIQUIDATION,
            value: StatusEnum.LIQUIDATION
        },
        {
            label: statusLabel.SAVE_STORAGE,
            value: StatusEnum.SAVE_STORAGE
        },
        {
            label: statusLabel.CANCEL,
            value: StatusEnum.CANCEL
        },
        {
            label: statusLabel.EXPIRED,
            value: StatusEnum.EXPIRED
        }
    ],
    obj: {
        [StatusEnum.DEPRECIATION]: {
            label: statusLabel.DEPRECIATION,
            value: StatusEnum.DEPRECIATION
        },
        [StatusEnum.ACTIVE]: {
            label: statusLabel.ACTIVE,
            value: StatusEnum.ACTIVE
        },
        [StatusEnum.LIQUIDATION]: {
            label: statusLabel.LIQUIDATION,
            value: StatusEnum.LIQUIDATION
        },
        [StatusEnum.SAVE_STORAGE]: {
            label: statusLabel.SAVE_STORAGE,
            value: StatusEnum.SAVE_STORAGE
        },
        [StatusEnum.CANCEL]:{
            label: statusLabel.CANCEL,
            value: StatusEnum.CANCEL
        },
        [StatusEnum.EXPIRED]:{
            label: statusLabel.EXPIRED,
            value: StatusEnum.EXPIRED
        }
    }
}