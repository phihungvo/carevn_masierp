import ButtonDelete from 'app/components/ButtonV2/ButtonDelete'
import ButtonEdit from 'app/components/ButtonV2/ButtonEdit'
import SplitButton from 'app/components/ButtonV2/SplitButton'
import CardV2 from 'app/components/CardV2/CardV2'
import Flex from 'app/components/flex/flex'
import InputSearch from 'app/components/input/input-search'
import { TableColumns } from 'app/components/table-v2/Table'
import TablePagination from 'app/components/table-v2/TablePagination'
import { Typography } from 'app/components/typography/typography'
import { DATE_FORMAT, isHasPermission } from 'app/constants/common'
import { PATH } from 'app/constants/path'
import useGoTo from 'app/hooks/use-go-to'
import { IIncomingInvoice } from 'app/shared/model/incoming-invoice.model'
import { convertCurrency, iconPath } from 'app/shared/util/format'
import dayjs from 'dayjs'
import { Link } from 'react-router-dom'
import { incomingStatus, incommingBadge, InvoiceType } from './constants/status'
import Filter from './Filter'
import useList from './useList'
import ButtonExcel from 'app/components/ButtonV2/ButtonExcel'
import { useExportIncommingInvoice, useIncomingInvoiceCancel } from './apis/api.hook'
import { useDownloadXlsx } from 'app/hooks/use-download'
import { useMutation } from '@tanstack/react-query'
import BadgeV2 from 'app/components/badge/badge-v2'
import useModalRedux from 'app/hooks/use-modal-redux'
import ButtonV2 from 'app/components/ButtonV2/ButtonV2'
import AuthGuard from 'app/components/guards/auth-guard'
import { useAppSelector } from 'app/config/store'

const importFormPath = `${PATH.INCOMING_INVOICE}/${PATH.IMPORT_FORM}`;
const normalFormPath = `${PATH.INCOMING_INVOICE}/${PATH.NORMAL_FORM}`;

const importFormPathDetail = (id: string) => `${PATH.INCOMING_INVOICE}/${PATH.IMPORT_FORM}/${id}`;
const normalFormPathDetail = (id: string) => `${PATH.INCOMING_INVOICE}/${PATH.NORMAL_FORM}/${id}`;

type Props = {}

const IncomingInvoiceV2 = (props: Props) => {
  // State
  const { goTo } = useGoTo()
  const { handleToggleModal, closeModal } = useModalRedux()

  const {
    handleSearch,
    incommingList,
    search,
    setQuery,
    handleQuery,
    query
  } = useList({});

  const cancelMutation = useIncomingInvoiceCancel({
    filter: query,
  })

  const { trigger, data: dataFile } = useExportIncommingInvoice();
  useDownloadXlsx(dataFile?.data, `HoaDonDauVao`, 'xlsx');

  // Function
  const onCancel = (id: string) => () => {
    handleToggleModal({
      okText: 'Xác nhận',
      cancelText: 'Đóng',
      title: 'Xác nhận',
      onOK: () => {
        cancelMutation.mutate(id)
        setTimeout(incommingList.refetch, 1000)
      },
      onCancel: closeModal,
      content: 'Bạn có chắc chắn muốn hủy hóa đơn này không?',
      isOpen: true
    })
  }

  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const columns: TableColumns<IIncomingInvoice> = [
    {
      header: {
        render: 'STT'
      },
      body: {
        render: ({ index }) => index + 1
      }
    },
    {
      header: {
        render: 'Số hóa đơn'
      },
      body: {
        render: ({ data }) => {
          let to = data?.invoiceType === InvoiceType.INVOICE ? normalFormPathDetail(data?.id) : importFormPathDetail(data?.id)
          return (
            <Link 
              to={to} 
              className='attachment-link'
              onClick={e => {
                if (!isHasPermission(authorities, 'INCOMING_INVOICE.VIEW')) e.preventDefault()
              }}
            >
              {data?.invoiceNo}
            </Link>
          )
        }
      }
    },
    {
      header: {
        render: 'Ngày PS'
      },
      body: {
        render: ({ data }) => dayjs(data?.createAt).format(DATE_FORMAT.DATE)
      }
    },
    {
      header: {
        render: 'Ngày hóa đơn'
      },
      body: {
        render: ({ data }) => dayjs(data?.invoiceDate).format(DATE_FORMAT.DATE)
      }
    },
    {
      header: {
        render: 'NCC'
      },
      body: {
        render: ({ data }) => data?.suppliers?.name
      }
    },
    {
      header: {
        render: 'MST'
      },
      body: {
        render: ({ data }) => data?.suppliers?.taxCode
      }
    },
    {
      header: {
        render: 'Số tiền'
      },
      body: {
        render: ({ data }) => convertCurrency(data?.totalAmount)
      }
    },
    {
      header: {
        render: 'VAT'
      },
      body: {
        render: ({ data }) => data?.totalVat
      }
    },
    {
      header: {
        render: 'Tổng tiền'
      },
      body: {
        render: ({ data }) => convertCurrency(data?.['grandTotal'])
      }
    },
    {
      header: {
        render: 'Đã thanh toán'
      },
      body: {
        render: ({ data }) => data?.['paymentRequest']?.['totalAmount']
      }
    },
    {
      header: {
        render: 'Diễn giải'
      },
      body: {
        render: ({ data }) => data?.['content']
      }
    },
    {
      header: {
        render: 'Trạng thái'
      },
      body: {
        render: ({ data }) => (
          <BadgeV2 color={incommingBadge?.[data?.status]}>{incomingStatus?.[data?.status]}</BadgeV2>
        )
      }
    },
    {
      header: {
        render: ''
      },
      body: {
        render: ({ data }) => {
          let to = data?.invoiceType === InvoiceType.INVOICE ? normalFormPathDetail(data?.id) : importFormPathDetail(data?.id)
          return (
            <>
              <AuthGuard permissionKey="INCOMING_INVOICE.VIEW">
                <ButtonEdit onClick={goTo(to)} />
              </AuthGuard>
              <AuthGuard permissionKey='INCOMING_INVOICE.EDIT'>
                <ButtonV2
                  onClick={data?.status !== 'CANCELLED' && onCancel(data?.id)}
                  disabled={data?.status === 'CANCELLED'}
                  variant="text"
                >
                  <img src={iconPath('close.svg')} alt="cancel invoice" />
                </ButtonV2>
              </AuthGuard>
            </>
          );
        }
      }
    },
  ]

  return (
    <CardV2
      header={
        <Flex justify="space-between">
          <Typography level={4}>Hóa đơn đầu vào</Typography>
          <AuthGuard permissionKey="INCOMING_INVOICE.CREATE">
            <SplitButton
              items={[
                { children: 'Hóa đơn', onClick: goTo(normalFormPath) },
                {
                  children: 'Hóa đơn nhập khẩu',
                  onClick: goTo(importFormPath),
                },
              ]}
            >
              Thêm
            </SplitButton>
          </AuthGuard>
        </Flex>
      }
    >
      <div>
        <Flex
          justify="space-between"
          align="center"
          style={{ marginBottom: '8px' }}
        >
          <InputSearch
            value={search?.['search']}
            onChange={handleSearch('search')}
          />
          <Flex gap={16}>
            <Filter setQuery={setQuery} />
            <AuthGuard permissionKey="INCOMING_INVOICE.EXPORT">
              <ButtonExcel onClick={trigger} />
            </AuthGuard>
          </Flex>
        </Flex>
        <TablePagination<any>
          table_id="purchase_proposal"
          columns={columns}
          data={incommingList?.data?.data}
          isLoading={incommingList?.isLoading}
          total_pages={incommingList?.data?.totalRecord}
          itemsPerPage={query.size}
          handlePageClick={handleQuery('page')}
          handlePageSizeChange={handleQuery('size')}
        />
      </div>
    </CardV2>
  );
}

export default IncomingInvoiceV2
