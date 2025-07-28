import ButtonAdd from 'app/components/ButtonV2/ButtonAdd'
import ButtonDelete from 'app/components/ButtonV2/ButtonDelete'
import TableV2, { TableColumns } from 'app/components/table-v2/Table'
import WrapInputNumber from 'app/components/wrap-input-text/WrapInputNumber'
import { clone, sumBy } from 'lodash'
import { useFormContext } from 'react-hook-form'
import WrapDate from '../../../../components/wrap-date/WrapDate'
import WrapInputText from '../../../../components/wrap-input-text/WrapInputText'
import { RelativedDocumentSchema } from '../../validations/relatived-document.validation'
import { convertCurrency } from 'app/shared/util/format'

type Props = {}

const RelativedDocument = (props: Props) => {

  const {
    watch, setValue, reset
  } = useFormContext<RelativedDocumentSchema>()
  let relativedDocument = watch('relativedDocument')

  const onAddRow = () => {
    relativedDocument = relativedDocument || []
    relativedDocument.push({
      price: 0,
      date: new Date().toISOString(),
    })
    setValue('relativedDocument', clone(relativedDocument))
  }

  const onDeleteRow = (index: number) => () => {
    relativedDocument = relativedDocument.filter((_, i) => i !== index)
    setValue('relativedDocument', clone(relativedDocument))
  }

  const columns: TableColumns<RelativedDocumentSchema> = [
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Nguồn'
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<RelativedDocumentSchema> name={`relativedDocument.${index}.resource`} />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 300
        },
        render: 'ID chứng từ'
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<RelativedDocumentSchema> name={`relativedDocument.${index}.documentCode`} />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 300
        },
        render: 'Số chứng từ'
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<RelativedDocumentSchema> name={`relativedDocument.${index}.documentNumber`} />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Ngày thêm'
      },
      body: {
        render: ({ data, index }) => (
          <WrapDate<RelativedDocumentSchema> name={`relativedDocument.${index}.date`} disabled />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Số tiền'
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputNumber<RelativedDocumentSchema> name={`relativedDocument.${index}.price`} />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Ghi chú'
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<RelativedDocumentSchema> name={`relativedDocument.${index}.note`} />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 100
        },
        render: ''
      },
      body: {
        render: ({ data, index }) => <ButtonDelete onClick={onDeleteRow(index)} />
      }
    },
  ]

  return (
    <TableV2<any>
      table_id='included-accessories'
      columns={columns}
      data={relativedDocument || []}
      tableFixedLayout={true}
      custom_body_row={() => {
        return (
          <tr>
            <td colSpan={3} />
            <td>Tổng</td>
            <td>{convertCurrency(sumBy(relativedDocument, (item) => item.price))}</td>
            <td colSpan={2}>
              <ButtonAdd text='Thêm' onClick={onAddRow} />
            </td>
          </tr>
        )
      }}
    />
  )
}

export default RelativedDocument