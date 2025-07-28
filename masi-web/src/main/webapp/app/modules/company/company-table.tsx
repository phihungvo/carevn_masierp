import ButtonV2 from 'app/components/ButtonV2/ButtonV2';
import EllipsisParagraph from 'app/components/ellipsis-paragraph/ellipsis-paragraph';
import Flex from 'app/components/flex/flex';
import AuthGuard from 'app/components/guards/auth-guard';
import { TableColumns } from 'app/components/table-v2/Table';
import TablePagination from 'app/components/table-v2/TablePagination';
import Tooltip from 'app/components/tooltip/tooltip';
import { useAppSelector } from 'app/config/store';
import { DEFAULT_PAGE, ICON_PATH, isHasPermission } from 'app/constants/common';
import { PATH } from 'app/constants/path';
import useCompany from 'app/hooks/use-company';
import { ICompany } from 'app/shared/model/company.model';
import { useContext } from 'react';
import { useNavigate } from 'react-router';
import { companyStatusBadge } from './company-mapping';
import { CompanyContext } from './company-provider';

const { useGetCompanies } = useCompany;

const CompanyTable = () => {
  const { filter, setFilter } = useContext(CompanyContext);

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const navigate = useNavigate();

  const { data } = useGetCompanies({ ...filter });

  const toUpdate = (id: string) => {
    navigate(PATH.COMPANY_UPDATE.replace(':id', id));
  };

  const columns: TableColumns<ICompany> = [
    {
      header: { render: 'Mã công ty' },
      body: {
        render: ({ data }) => {
          const value = data?.name;
          return (
            <p
              className="attachment-link"
              onClick={() => {
                if (!isHasPermission(authorities, 'COMPANY.EDIT')) return;
                toUpdate(data.id);
              }}
            >
              <EllipsisParagraph
                text={value}
                width={160}
                id={`dateRecord-${data.id}`}
              />
            </p>
          );
        },
      },
    },
    {
      header: { render: 'Công ty' },
      body: {
        render: ({ data }) => {
          const value = data?.name;
          return (
            <Tooltip label={value} target={`name-${data?.id}`}>
              <EllipsisParagraph
                text={value}
                width={150}
                id={`name-${data?.id}`}
              />
            </Tooltip>
          );
        },
      },
    },
    {
      header: { render: 'MST' },
      body: {
        render: ({ data }) => {
          const value = data?.name;
          return (
            <Tooltip label={value} target={`name-${data?.id}`}>
              <EllipsisParagraph
                text={value}
                width={100}
                id={`name-${data?.id}`}
              />
            </Tooltip>
          );
        },
      },
    },
    {
      header: { render: 'Địa chỉ' },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.name} target={`name-${data?.id}`}>
            <EllipsisParagraph
              text={data?.name}
              width={200}
              id={`name-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Người đại diện' },
      body: {
        render: ({ data }) => (
          <Tooltip label={data?.name} target={`content-${data?.id}`}>
            <EllipsisParagraph
              text={data?.name}
              width={200}
              id={`content-${data?.id}`}
            />
          </Tooltip>
        ),
      },
    },
    {
      header: { render: 'Trạng thái' },
      body: {
        render: ({ data }) => companyStatusBadge(data?.isActivated),
      },
    },
    {
      header: { render: '' },
      body: {
        render: ({ data }) => (
          <Flex align="center">
            <AuthGuard permissionKey="COMPANY.VIEW">
              <ButtonV2 variant="text" onClick={() => toUpdate(data?.id)}>
                <img src={ICON_PATH + 'edit-3.svg'} alt="edit" />
              </ButtonV2>
            </AuthGuard>
          </Flex>
        ),
      },
    },
  ];

  const handlePageChange = (page: number) => {
    setFilter({ ...filter, page: page });
  };

  const handlePageSizeChange = (pageSize: number) => {
    setFilter({ ...filter, page: DEFAULT_PAGE, size: pageSize });
  };

  return (
    <TablePagination<ICompany>
      table_id="complains"
      columns={columns}
      data={data?.data ?? []}
      total_pages={data?.totalRecord ?? 0}
      itemsPerPage={filter?.size}
      handlePageClick={handlePageChange}
      handlePageSizeChange={handlePageSizeChange}
    />
  );
};

export default CompanyTable;
