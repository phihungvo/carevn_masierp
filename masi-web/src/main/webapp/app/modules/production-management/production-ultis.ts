import { MANUFACTURE_ORDER_STATUS } from 'app/shared/model/enumerations/production-command.model';

export const enableDirectMaterial = (statusWatch: string) =>
  statusWatch === (MANUFACTURE_ORDER_STATUS.NEW as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.ADDITIVES as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.PRODUCTION as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.PACKAGING as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.SHIPPED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

export const enableDirectAdditives = (statusWatch: string) =>
  statusWatch === (MANUFACTURE_ORDER_STATUS.ADDITIVES as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.PRODUCTION as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.PACKAGING as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.SHIPPED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

export const enableDirectProduction = (statusWatch: string) =>
  statusWatch === (MANUFACTURE_ORDER_STATUS.PRODUCTION as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.PACKAGING as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.SHIPPED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

export const enableDirectPackaging = (statusWatch: string) =>
  statusWatch === (MANUFACTURE_ORDER_STATUS.PACKAGING as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.SHIPPED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

export const enableDirectShipment = (statusWatch: string) =>
  statusWatch === (MANUFACTURE_ORDER_STATUS.PACKED_COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.SHIPPED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

export const enableDirectImport = (statusWatch: string) =>
  statusWatch === (MANUFACTURE_ORDER_STATUS.SHIPPED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);

export const enable = (statusWatch: string) =>
  statusWatch === (MANUFACTURE_ORDER_STATUS.COMPLETED as string) ||
  statusWatch === (MANUFACTURE_ORDER_STATUS.CANCELLED as string);
