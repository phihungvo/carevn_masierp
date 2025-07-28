import ButtonDelete from "app/components/ButtonV2/ButtonDelete"
import ButtonV2 from "app/components/ButtonV2/ButtonV2"
import Flex from "app/components/flex/flex"
import { Typography } from "app/components/typography/typography"
import './ContractItem.scss'

type Props = {
  label: string,
  code: string,
  onAdd?: () => void
  onRemove?: () => void
}

const ContractItem = (props: Props) => {
  const { code, label, onAdd, onRemove } = props

  return (
    <div className="contract-item">
      <Flex justify="space-between" align="center">
        <Typography level={6}>
          {label}
        </Typography>
        <ButtonV2 variant="fill" color="blue" onClick={onAdd}>
          Thêm
        </ButtonV2>
      </Flex>
      <Flex justify="space-between" align="center">
        <span className="contract-item__code">{code}</span>
        <ButtonDelete onClick={onRemove} />
      </Flex>
    </div>
  )
}

export default ContractItem
