export interface IInvalidPhoneNumber {
  name?: string;
  family?: string;
  phoneNumber?: string;
  reason?: string;
}

export class InvalidPhoneNumberModel implements IInvalidPhoneNumber {
  constructor(public name?: string, public family?: string, public phoneNumber?: string, public reason?: string) {}
}

export function getInvalidPhoneNumberModelIdentifier(invalidPhoneNumberModel: IInvalidPhoneNumber): string | undefined {
  return invalidPhoneNumberModel.phoneNumber;
}
