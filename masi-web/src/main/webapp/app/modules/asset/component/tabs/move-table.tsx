import TableV2, { TableColumns } from 'app/components/table-v2/Table'
import PersonSelect from 'app/components/wrap-select/Person'
import WorkSpace from 'app/components/wrap-select/Workspace'
import { clone } from 'lodash'
import { useFormContext } from 'react-hook-form'
import { useParams } from 'react-router'
import WrapDate from '../../../../components/wrap-date/WrapDate'
import WrapInputText from '../../../../components/wrap-input-text/WrapInputText'
import { useArisesDetail } from '../../apis/api.hook'
import { Move, MoveSchemaType } from '../../validations/move.validation'

type Props = {}

const Move = (props: Props) => {

  const {
    watch, setValue, reset, getValues
  } = useFormContext<MoveSchemaType>()
  let move = getValues('move')

  const params = useParams()
  const id = params?.id

  useArisesDetail(id)

  const onDeleteRow = (index: number) => () => {
    move = move.filter((_, i) => i !== index)
    setValue('move', clone(move))
  }

  const columns: TableColumns<Move> = [
    {
      header: {
        th_style: {
          width: 200
        },
        render: 'Ngày'
      },
      body: {
        render: ({ data, index }) => (
          <WrapDate<MoveSchemaType> name={`move.${index}.date`} disabled />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 250
        },
        render: 'Tham chiếu'
      },
      body: {
        render: ({ data, index }) => (
          <WrapInputText<MoveSchemaType> name={`move.${index}.reflect`} disabled />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 250
        },
        render: 'Từ vị trí'
      },
      body: {
        render: ({ data, index }) => (
          <WorkSpace<MoveSchemaType> name={`move.${index}.fromPosition`} isDisabled />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 250
        },
        render: 'Đến vị trí'
      },
      body: {
        render: ({ data, index }) => (
          <WorkSpace<MoveSchemaType> name={`move.${index}.toPosition`} isDisabled />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 250
        },
        render: 'Từ người'
      },
      body: {
        render: ({ data, index }) => (
          <PersonSelect<MoveSchemaType> 
            name={`move.${index}.fromEmployee`}
            isDisabled
          />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 250
        },
        render: 'Đến người'
      },
      body: {
        render: ({ data, index }) => (
          <PersonSelect<MoveSchemaType>
            name={`move.${index}.toEmployee`}
            isDisabled
          />
        )
      }
    },
    {
      header: {
        th_style: {
          width: 250
        },
        render: 'Người đổi'
      },
      body: {
        render: ({ data, index }) => (
          <PersonSelect<MoveSchemaType> 
            name={`move.${index}.personChange`} 
            isDisabled
          />
        )
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
        render: ({ data, index }) => (
          <WrapInputText<MoveSchemaType> 
            name={`move.${index}.note`} 
            disabled
          />
        )
      }
    },
  ]

  return (
    <TableV2<any>
      table_id='included-accessories'
      columns={columns}
      data={move || []}
      tableFixedLayout={true}
    />
  )
}

export default Move