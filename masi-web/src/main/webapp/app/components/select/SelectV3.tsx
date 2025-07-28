import { ComponentProps } from "react"
import Select from "./select"

type Props = ComponentProps<typeof Select>

const SelectV3 = (props: Props) => {
  return (
    <Select
      placeholder="Chọn"
      {...props}
      menuPortalTarget={document.body}
      isSearchable
      styles={{
        menuPortal: base => ({ ...base, zIndex: 9999 }),
        input: base => ({ ...base, flexGrow: 1 }),
      }}
      isClearable
    />
  )
}

export default SelectV3
