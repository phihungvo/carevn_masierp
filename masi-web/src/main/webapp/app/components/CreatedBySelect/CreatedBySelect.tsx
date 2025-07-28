import useAccountApp from "app/hooks/use-account-app"
import { ComponentProps } from "react"
import SelectV3 from "../select/SelectV3"
import useEmployee from "app/hooks/use-employee"
import { MapKeySelect } from "app/shared/model/arr-obj.model"

const {
  useGetEmployeesQuery
} = useEmployee

type Props = ComponentProps<typeof SelectV3> & {
  selectedKey?: string
}

const CreatedBySelect = (props: Props) => {
  const { selectedKey, ...rest } = props;

  const employees: MapKeySelect = useGetEmployeesQuery({}, (res) => {
    let data = res?.data
    let arr = []
    let obj = {}
    if (!data) return { arr: [], obj: {}, totalRecord: 0 }
    data?.data?.forEach((item) => {
      let tmp = {
        label: item?.code + ' - ' + item?.lastName + ' ' + item?.firstName,
        value: item?.id
      }
      arr.push(tmp)
      obj[tmp.value] = tmp
    })
    return {
      arr,
      obj,
      totalRecord: data?.totalRecord
    } as any
  })

  return (
    <SelectV3
      placeholder="Nhập"
      value={selectedKey && [{
        label: employees?.data?.obj?.[selectedKey]?.label,
        value: selectedKey
      }]}
      {...rest}
      options={employees?.data?.arr || []}
    />
  )
}

export default CreatedBySelect
