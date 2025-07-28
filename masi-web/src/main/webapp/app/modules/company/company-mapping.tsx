import BadgeV2 from 'app/components/badge/badge-v2';

export const companyStatusBadge = (key: boolean) => {
  switch (key) {
    case true:
      return <BadgeV2 className="bv2 pr-approved">Hoạt động</BadgeV2>;
    case false:
      return <BadgeV2 className="bv2 pr-closed">Không hoạt động</BadgeV2>;
    default:
      break;
  }
};
