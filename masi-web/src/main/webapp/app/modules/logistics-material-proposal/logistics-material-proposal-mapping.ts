import { Color } from 'app/shared/model/enumerations/color.model';
import { LOGISTICS_MATERIAL_RPOPOSAL_STATUS } from 'app/shared/model/enumerations/logistics-material-proposal';

const logisticsMaterialProposalColorMapping = (text: LOGISTICS_MATERIAL_RPOPOSAL_STATUS): Color => {
    switch (text) {
        case LOGISTICS_MATERIAL_RPOPOSAL_STATUS.NEW:
            return Color.PRIMARY;
        case LOGISTICS_MATERIAL_RPOPOSAL_STATUS.WAITING_APPROVAL:
            return Color.WARNING;
        case LOGISTICS_MATERIAL_RPOPOSAL_STATUS.APPROVED:
            return Color.SUCCESS;
        case LOGISTICS_MATERIAL_RPOPOSAL_STATUS.REJECTED:
            return Color.ERROR;
        case LOGISTICS_MATERIAL_RPOPOSAL_STATUS.IS_DELIVERING:
            return Color.WARNING;
        default:
            return undefined;
    }
};

const logisticsMaterialProposalTextMapping = (text: LOGISTICS_MATERIAL_RPOPOSAL_STATUS): string => {
    switch (text) {
        case LOGISTICS_MATERIAL_RPOPOSAL_STATUS.NEW:
            return 'Mới';
        case LOGISTICS_MATERIAL_RPOPOSAL_STATUS.WAITING_APPROVAL:
            return 'Đợi duyệt';
        case LOGISTICS_MATERIAL_RPOPOSAL_STATUS.APPROVED:
            return 'Đã duyệt';
        case LOGISTICS_MATERIAL_RPOPOSAL_STATUS.REJECTED:
            return 'Từ chối';
        case LOGISTICS_MATERIAL_RPOPOSAL_STATUS.IS_DELIVERING:
            return 'Đang giao';
        case LOGISTICS_MATERIAL_RPOPOSAL_STATUS.COMPLETED:
            return 'Hoàn tất';
        default:
            return '';
    }
};

export default {
    logisticsMaterialProposalColorMapping,
    logisticsMaterialProposalTextMapping,
};
