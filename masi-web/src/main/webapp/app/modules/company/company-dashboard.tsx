import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import CardV2 from 'app/components/CardV2/CardV2';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import InputSearch from 'app/components/input/input-search';
import { Typography } from 'app/components/typography/typography';
import { DEFAULT_PAGE, ICON_PATH } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import { useDebounce } from 'app/hooks/use-debounce';
import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { CompanyContext } from './company-provider';
import CompanyTable from './company-table';
import CompanyActiveSuccessModal from './modals/company-active-success-modal';
import CompanyActiveModal from './modals/company-confirm-active-modal';
import CompanyInactiveModal from './modals/company-confirm-inactive-modal';
import CompanyFilterModals from './modals/company-filter-modals';
import CompanyInactiveSuccessModal from './modals/company-inactive-success-modal';

const CompanyDashboard = () => {
  const navigate = useNavigate();

  const { toggleFilter, setFilter } = useContext(CompanyContext);

  const [searchText, setSearchText] = useState('');
  const searchDebounce = useDebounce(searchText, 500);

  useEffect(() => {
    setFilter(prev => ({
      ...prev,
      page: DEFAULT_PAGE,
      search: searchDebounce === '' ? undefined : searchDebounce,
      'code.contains': searchDebounce === '' ? undefined : searchDebounce,
    }));
  }, [searchDebounce]);

  return (
    <>
      <CardV2
        header={
          <Flex justify="space-between" align="center">
            <Typography level={4}>Công ty</Typography>
            <AuthGuard permissionKey="COMPANY.CREATE">
              <ButtonV2
                color="blue"
                variant="solid"
                onClick={() => navigate(PATH.COMPANY_CREATE)}
              >
                Thêm
              </ButtonV2>
            </AuthGuard>
          </Flex>
        }
      >
        <div className="rp__container">
          <Flex
            className="rp__header"
            justify="space-between"
            align="center"
            style={{ marginBottom: 12 }}
          >
            <InputSearch onChange={e => setSearchText(e.target.value)} />
            {/* <Flex gap={16}>
              <ButtonV2
                left_section={
                  <img src={ICON_PATH + 'three-line-filter.svg'} alt="filter" />
                }
                onClick={toggleFilter}
              >
                Bộ lọc
              </ButtonV2>
            </Flex> */}
          </Flex>
          <CompanyTable />
        </div>
      </CardV2>

      <CompanyFilterModals />

      <CompanyInactiveModal />
      <CompanyInactiveSuccessModal />

      <CompanyActiveModal />
      <CompanyActiveSuccessModal />
    </>
  );
};

export default CompanyDashboard;
