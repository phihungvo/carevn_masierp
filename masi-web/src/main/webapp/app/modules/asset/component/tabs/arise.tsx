import TableV2, { TableColumns } from 'app/components/table-v2/Table'
import { DATE_FORMAT } from 'app/constants/common'
import dayjs from 'dayjs'
import { sumBy } from 'lodash'
import { useFormContext } from 'react-hook-form'
import { useParams } from 'react-router'
import { useArisesDetail } from '../../apis/api.hook'
import { Arise, AriseSchemaType } from '../../validations/arise.validation'
import { convertCurrency } from 'app/shared/util/format'

type Props = {}

const Arise = (props: Props) => {
  const params = useParams()
  const id = params?.id

  const {
    watch, setValue, reset
  } = useFormContext<AriseSchemaType>()
  let arises = watch('arises')

  const ariseQuery = useArisesDetail(id)

  const columns: TableColumns<any> = [
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Nghiệp vụ'
      },
      body: {
        render: ({ data, index }) => data?.job
      }
    },
    {
      header: {
        th_style: {
          width: 300
        },
        render: 'Ngày'
      },
      body: {
        render: ({ data, index }) => dayjs(data?.date).format(DATE_FORMAT.DATE)
      }
    },
    {
      header: {
        th_style: {
          width: 250
        },
        render: 'Số tham chiếu'
      },
      body: {
        render: ({ data, index }) => data?.reflectNumber
      }
    },
    {
      header: {
        th_style: {
          width: 150
        },
        render: 'TTCP'
      },
      body: {
        render: ({ data, index }) => data?.ttcp
      }
    },
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Số lượng'
      },
      body: {
        render: ({ data, index }) => data?.quantity ? convertCurrency(data?.quantity) : ''
      }
    },
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Nguyên giá'
      },
      body: {
        render: ({ data, index }) => data?.price ? convertCurrency(data?.price) : ''
      }
    },
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Khấu hao'
      },
      body: {
        render: ({ data, index }) => data?.depriciation ? convertCurrency(data?.depriciation) : ''
      }
    },
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Tháng KH'
      },
      body: {
        render: ({ data, index }) => data?.KhMonth
      }
    },
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Diễn giải'
      },
      body: {
        render: ({ data, index }) => data?.note
      }
    },
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Từ mã gốc'
      },
      body: {
        render: ({ data, index }) => data?.recourseCode
      }
    },
  ]

  return (
    <TableV2<any>
      table_id='included-accessories'
      columns={columns}
      data={ariseQuery?.data}
      tableFixedLayout={true}
      custom_body_row={() => {
        return (
          <tr>
            <td colSpan={5} />
            <td>Tổng</td>
            <td>{convertCurrency(sumBy(ariseQuery?.data, (item) => item?.depriciation || 0))}</td>
            <td colSpan={3} />
          </tr>
        )
      }}
    />
  )
}

export default Arise