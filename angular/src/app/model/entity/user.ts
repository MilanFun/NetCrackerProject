export interface User {
  id: bigint;
  email: string;
  login: string;
  encodedPassword: string;
  firstName: string;
  lastName: string;
  rating: number;
  points: number;
  group: string;
  vkLink: string;
  telegramLink: string;
  phoneNumber: string;
  attendedCount: number;
  conductedCount: number;
}
